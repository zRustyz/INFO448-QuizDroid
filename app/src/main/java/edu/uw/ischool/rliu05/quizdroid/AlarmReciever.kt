package edu.uw.ischool.rliu05.quizdroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReciever : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Reciever", "Recieved Broadcast, starting service")
        context?.startService(Intent(context, DownloadQuestions::class.java))
    }
}