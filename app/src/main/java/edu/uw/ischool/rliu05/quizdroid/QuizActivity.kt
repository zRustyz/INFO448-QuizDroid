package edu.uw.ischool.rliu05.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_layout)
        val textViewQuestion: TextView = findViewById(R.id.TextViewQuestion)
        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        val btnSubmit: Button = findViewById(R.id.buttonSubmit)
        val choice1: RadioButton = findViewById(R.id.radioButton)
        val choice2: RadioButton = findViewById(R.id.radioButton1)
        val choice3: RadioButton = findViewById(R.id.radioButton2)
        val choice4: RadioButton = findViewById(R.id.radioButton3)
        val clickedItem = intent.getStringExtra("clickedItem")
        val questionNumber = intent.getIntExtra("questionNum", 0)
        var correctAns = intent.getIntExtra("correctAns", 0)
        val quiz = (application as QuizApp).repository.getTopic(clickedItem!!).question[questionNumber]
        textViewQuestion.text = quiz.question
        choice1.text = quiz.answers[0]
        choice2.text = quiz.answers[1]
        choice3.text = quiz.answers[2]
        choice4.text = quiz.answers[3]
        btnSubmit.isEnabled = false
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            btnSubmit.isEnabled = selectedRadioButton != null
        }
        btnSubmit.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            val selectedText = findViewById<RadioButton>(selectedId).text
            val intent = Intent(this, AnswerActivity::class.java).apply {
                putExtra("questionNum", questionNumber)
                putExtra("correctAns", correctAns)
                putExtra("clickedItem", clickedItem)
                putExtra("selectedChoice", selectedText)
            }
            startActivity(intent)
        }
    }
}