package edu.uw.ischool.rliu05.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnswerActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.answer_layout)
        val yourAns: TextView = findViewById(R.id.textViewYou)
        val correctAnswer: TextView = findViewById(R.id.textViewCorrect)
        val score: TextView = findViewById(R.id.textViewNumCorrect)
        val btn: Button = findViewById(R.id.buttonNext)
        val clickedItem = intent.getStringExtra("clickedItem")
        var questionNumber = intent.getIntExtra("questionNum", 0)
        var correctAns = intent.getIntExtra("correctAns", 0)
        var selectedText = intent.getStringExtra("selectedChoice")
        val topic = (application as QuizApp).repository.getTopic(clickedItem!!)
        val quiz = topic.question[questionNumber]
        val correct = quiz.choices[quiz.correctAns]
        if (selectedText == correct) {
            correctAns++
        }
        questionNumber += 1
        yourAns.text = selectedText
        correctAnswer.text = correct
        score.text = "You have $correctAns out of $questionNumber correct"
        if (questionNumber == topic.question.size) {
            btn.text = "Finish"
            btn.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        } else {
            btn.setOnClickListener {
                val intent = Intent(this, QuizActivity::class.java).apply {
                    putExtra("questionNum", questionNumber)
                    putExtra("correctAns", correctAns)
                    putExtra("clickedItem", clickedItem)
                }
                startActivity(intent)
            }
        }

    }
}