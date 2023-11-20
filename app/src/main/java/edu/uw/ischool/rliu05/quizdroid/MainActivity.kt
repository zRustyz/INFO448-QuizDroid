package edu.uw.ischool.rliu05.quizdroid

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.FileReader


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
        val externalFileDir = getExternalFilesDir("QuizDroidRepo")
        Log.i("FileReader", "Files Dir is $externalFileDir")
        try {

            val reader = FileReader(externalFileDir.toString() + "/Questions.txt")
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
        repository = TestRepo(repoMap)

        Log.i("QuizApp", "Quiz app loaded")

    }
}



class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewQuizTopics: RecyclerView
    private lateinit var adapter: Adapter


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
    }
}