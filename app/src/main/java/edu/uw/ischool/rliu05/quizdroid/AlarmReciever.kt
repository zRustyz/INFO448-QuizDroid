package edu.uw.ischool.rliu05.quizdroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
class AlarmReciever : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        // Handle the alarm event here, such as triggering the network request
        // You can start an IntentService or call your network request function here
        // For example, start a service to perform the task:
        context?.startService(Intent(context, DownloadQuestions::class.java))
    }
}