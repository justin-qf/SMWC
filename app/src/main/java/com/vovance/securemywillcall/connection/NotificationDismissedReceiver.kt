package com.vovance.securemywillcall.connection

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationDismissedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.extras!!.getInt("notificationId")
        /* Your code to handle the event here */
        if (notificationId == 0) {
            val notifyManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifyManager.cancel(0)
            notifyManager.cancelAll()
        }
    }
}