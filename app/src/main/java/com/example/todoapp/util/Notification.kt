package com.example.todoapp.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.todoapp.R
import com.example.todoapp.util.Constants.CHANNEL_ID
import com.example.todoapp.util.Constants.MESSAGE_EXTRA
import com.example.todoapp.util.Constants.NOTIFICATION_ID
import com.example.todoapp.util.Constants.TITLE_EXTRA

class Notification: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notification = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.drawable.to_do_list)
            .setContentTitle(intent.getStringExtra(TITLE_EXTRA))
            .setContentText(intent.getStringExtra(MESSAGE_EXTRA))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID,notification)
    }
}