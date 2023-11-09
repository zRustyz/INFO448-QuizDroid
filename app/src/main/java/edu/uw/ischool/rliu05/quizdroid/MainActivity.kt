package edu.uw.ischool.rliu05.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.app.Application



data class Quiz(val question: String,
                val choices: Array<String>,
                val correctAns: Int)

data class Topic (
                 val title: String,
                 val shortDesc: String,
                 val longDesc: String,
                 val question: Array<Quiz>)

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
    val math1 = Quiz("1+1", arrayOf<String>("1", "2", "3", "4"), 1)
    val math2 = Quiz("3-1", arrayOf<String>("1", "2", "3", "4"), 1)
    val math3 = Quiz("1x1", arrayOf<String>("1", "2", "3", "4"), 0)
    val math4 = Quiz("4/1", arrayOf<String>("1", "2", "3", "4"), 3)
    val phyc1 = Quiz("What's the earth's gravity in m/s", arrayOf<String>("0", "2", "3", "9.8"), 3)
    val phyc2 = Quiz("Who published the theory of relativity", arrayOf<String>("Newton", "Einstein", "Galileo", "Ted Neward"), 1)
    val phyc3 = Quiz("Who discovered the gravity", arrayOf<String>("Newton", "Einstein", "Galileo", "Ted Neward"), 0)
    val phyc4 = Quiz("What fell on Newton's head", arrayOf<String>("orange", "lemon", "apple", "bannana"), 2)
    val marv1 = Quiz("Who is the angry green superhero", arrayOf<String>("hulk", "ironman", "spiderman", "antman"), 0)
    val marv2 = Quiz("Who is the rich billionaire that wears a high tech suit", arrayOf<String>("hulk", "ironman", "spiderman", "antman"), 1)
    val marv3 = Quiz("Who is the kid from queens that got bite by a spider", arrayOf<String>("hulk", "ironman", "spiderman", "antman"), 2)
    val marv4 = Quiz("Who is the superhero that can make himself smaller than an ant", arrayOf<String>("hulk", "ironman", "spiderman", "antman"), 3)
    val math = Topic("math", "Quiz About math", "Super duper ultra difficult quiz about math", arrayOf(math1, math2, math3, math4))
    val phyc = Topic("physics", "Quiz About physics", "Super duper ultra difficult quiz about physics", arrayOf(phyc1, phyc2, phyc3, phyc4))
    val marv = Topic("marvel super heros", "Quiz About marvel superheros", "Super duper ultra difficult quiz about marvel superheros", arrayOf(marv1, marv2, marv3, marv4))


    var repository : TopicRepository = TestRepo(mapOf<String,Topic>("math" to math, "physics" to phyc, "marvel super heros" to marv))
    override fun onCreate() {
        super.onCreate()
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