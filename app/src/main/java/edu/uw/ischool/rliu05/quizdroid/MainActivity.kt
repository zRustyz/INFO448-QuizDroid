package edu.uw.ischool.rliu05.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewQuizTopics: RecyclerView
    private lateinit var questions: ArrayList<String>
    private lateinit var adapter: Adapter
    private lateinit var buttonBegin: Button
    private lateinit var radioGroup: RadioGroup
    private val TAG: String = "quizDroid"
    private var questionNumber: Int = 0
    private lateinit var quizType: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerViewQuizTopics = findViewById(R.id.recyclerViewQuizTopics)

        questions = ArrayList<String>(listOf("Math", "Physics", "Marvel Super Heroes"))

        adapter = Adapter(this, questions) { clickedItem ->
            Log.i(TAG, "clicked on $clickedItem")
            quizType = clickedItem
            setContentView(R.layout.description)
            val overview: TextView = findViewById(R.id.textViewOverview)
            val description: TextView = findViewById(R.id.textViewDescription)
            buttonBegin = findViewById(R.id.buttonBegin)
            overview.text = clickedItem
            description.text = "Quiz about $clickedItem"
        }



        val gridLayoutManager: GridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)

        recyclerViewQuizTopics.layoutManager = gridLayoutManager
        recyclerViewQuizTopics.adapter = adapter
    }
}