package edu.uw.ischool.rliu05.quizdroid

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors


data class Questions(val question: String,
                     val answers: Array<String>,
                     val answer: Int)

data class Topic (
                 val title: String,
                 val desc: String,
                 val question: Array<Questions>)

interface TopicRepository {
    fun getTopic(title: String): Topic
    fun getKeys(): ArrayList<String>
}
class TestRepo (val testData : Map<String, Topic>) : TopicRepository {
    override fun getTopic(title: String): Topic {
        return testData[title]!!
    }

    override fun getKeys(): ArrayList<String> {
        return ArrayList(testData.keys)
    }

}
class QuizApp : Application() {

    var repoMap: MutableMap<String, Topic> = mutableMapOf<String, Topic>()
    lateinit var repository : TopicRepository


    override fun onCreate() {
        super.onCreate()
        runBlocking {
            download()
            update()
        }
        repository = TestRepo(repoMap)
    }

    fun download() {
         val executor : Executor = Executors.newSingleThreadExecutor()
         executor.execute {
             val url = URL("http", "tednewardsandbox.site44.com", 80, "/questions.json")
             val urlConnection = url.openConnection() as HttpURLConnection
             if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                 val inputStream = urlConnection.getInputStream()
                 val reader = InputStreamReader(inputStream)
                 val fileName = "Questions.json"
                 val folder = getExternalFilesDir("QuizDroidRepo")
                 val file = File(folder, fileName)
                 val outputStream = FileOutputStream(file)
                 outputStream.write(reader.readText().toByteArray())
                 reader.close()
                 outputStream.close()
             }
         }
    }

    suspend fun update() {
        delay(1000)
        val externalFileDir = getExternalFilesDir("QuizDroidRepo")
        Log.i("FileReader", "Files Dir is $externalFileDir")
        try {
            val reader = FileReader(externalFileDir.toString() + "/Questions.json")
            val jsonArray = JSONArray(reader.readText())
            Log.i("JSONArray", "Json content is $jsonArray")
            reader.close()
            for (i in 0 until jsonArray.length()) {
                val topicObject = jsonArray.getJSONObject(i)

                val title = topicObject.getString("title")
                val desc = topicObject.getString("desc")

                val questionsJSONArray = topicObject.getJSONArray("questions")
                // Iterate through questions for each topic

                val questionsArray: Array<Questions> = Array(questionsJSONArray.length()) {
                    val questionObject = questionsJSONArray.getJSONObject(it)
                    val text = questionObject.getString("text")
                    val answer: Int = questionObject.getString("answer").toInt() - 1
                    val answersJSONArray = questionObject.getJSONArray("answers")
                    val answersArray = Array(answersJSONArray.length()) {
                        answersJSONArray.getString(it)
                    }
                    Questions(text, answersArray, answer)
                }
                repoMap.put(title, Topic(title, desc, questionsArray))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.i("QuizApp", "Quiz app loaded")
    }
}



class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewQuizTopics: RecyclerView
    private lateinit var adapter: Adapter
    private var interval: Long = 60000



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewQuizTopics = findViewById(R.id.recyclerViewQuizTopics)

        adapter = Adapter(this, (application as QuizApp).repository.getKeys()) { clickedItem ->
            val intent = Intent(this, DescriptionActivity::class.java).apply {
                putExtra("clickedItem", clickedItem)
            }
            startActivity(intent)
            Log.i("Main", "clicked on $clickedItem")
        }

        val gridLayoutManager: GridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)

        recyclerViewQuizTopics.layoutManager = gridLayoutManager
        recyclerViewQuizTopics.adapter = adapter
        startBackgroundService()
    }

    private fun startBackgroundService() {
        val activityThis = this
        val alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activityThis, AlarmReciever::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activityThis, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val firstTriggerAtMillis = System.currentTimeMillis()  + interval
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
            firstTriggerAtMillis,
            interval,
            pendingIntent)
    }
}