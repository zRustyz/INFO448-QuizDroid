package edu.uw.ischool.rliu05.quizdroid

import android.app.IntentService
import android.content.Intent
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DownloadQuestions : IntentService("DownloadQuestion") {
    override fun onHandleIntent(intent: Intent?) {
        val executor : Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val url = URL("http", intent?.getStringExtra("url"), 80, "/questions.json")
            val urlConnection = url.openConnection() as HttpURLConnection
            if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = urlConnection.getInputStream()
                val reader = InputStreamReader(inputStream)
                val fileName = "Questions.json"
                Log.i("Download", "Downloading and writing JSON")
                val folder = getExternalFilesDir("QuizDroidRepo")
                val file = File(folder, fileName)
                val outputStream = FileOutputStream(file)
                outputStream.write(reader.readText().toByteArray())
                reader.close()
                outputStream.close()
            }
        }
    }

}