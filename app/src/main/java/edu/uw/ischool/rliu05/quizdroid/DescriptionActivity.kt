package edu.uw.ischool.rliu05.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.description)
        val overview: TextView = findViewById(R.id.textViewOverview)
        val description: TextView = findViewById(R.id.textViewDescription)
        val buttonBegin: Button = findViewById(R.id.buttonBegin)
        val clickedItem = intent.getStringExtra("clickedItem")
        val questionNum = 0

        val topic = (application as QuizApp).repository.getTopic(clickedItem!!)

        overview.text = topic.shortDesc
        description.text = topic.longDesc
        buttonBegin.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java).apply {
                putExtra("questionNum", 0)
                putExtra("correctAns", 0)
                putExtra("clickedItem", clickedItem)
            }
            startActivity(intent)
        }
    }
}