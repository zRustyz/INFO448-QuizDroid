package edu.uw.ischool.rliu05.quizdroid

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Prefs : AppCompatActivity() {

    lateinit var editTextUrl: TextView
    lateinit var editTextMin: TextView
    lateinit var buttonApply: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.prefs_layout)
        editTextUrl = findViewById(R.id.editTextUrl)
        editTextMin = findViewById(R.id.editTextMin)
        editTextUrl.addTextChangedListener(watcher)
        editTextMin.addTextChangedListener(watcher)
        buttonApply = findViewById(R.id.button3)
        buttonApply.setOnClickListener {
            startBackgroundService()
            Toast.makeText(this, "Fetching questions.json from ${editTextUrl.text.toString()}/questions.json", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun startBackgroundService() {
        val activityThis = this
        val url = editTextUrl.text.toString()
        val interval = editTextMin.text.toString().toLong() * 60000
        val alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activityThis, AlarmReciever::class.java).apply {
            putExtra("url", url)
        }
        val pendingIntent = PendingIntent.getBroadcast(activityThis, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val firstTriggerAtMillis = System.currentTimeMillis()
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            firstTriggerAtMillis,
            interval,
            pendingIntent)
    }


    private val watcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val urlValid = editTextUrl.text.isNotEmpty()
            val minutesValid = editTextMin.text.isNotEmpty()

            buttonApply.isEnabled = urlValid && minutesValid
        }

    }
}