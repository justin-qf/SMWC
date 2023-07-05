package com.vovance.securemywillcall.connection

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.vovance.securemywillcall.R
import com.vovance.securemywillcall.activity.MainActivity
import com.vovance.securemywillcall.activity.SplashActivity
import com.vovance.securemywillcall.common.Constant
import com.vovance.securemywillcall.common.HELPER
import com.vovance.ssn.Common.Pref
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var GROUP_KEY = "com.vovance.SecureMyWillCall.user.GROUP"

    @Override
    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }

    @Override
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        try {
            HELPER.print("NotificationsResponse:1", Gson().toJson(remoteMessage.notification))
            HELPER.print("NotificationsResponse:2", Gson().toJson(remoteMessage.data))
            if (remoteMessage.notification != null) {
                showNotification(
                    remoteMessage.data["status"],
                    remoteMessage.notification!!.title!!,
                    remoteMessage.notification!!.body
                )
            } else {
                showNotification(
                    "0",
                    Constant.CHANNEL_ID,
                    "Thanks for using app"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showNotification(
        status: String?,
        title: String?,
        msg: String?,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel =
                NotificationChannel(Constant.CHANNEL_ID, Constant.CHANNEL_NAME, importance)
            mChannel.description = Constant.CHANNEL_DESCRIPTION
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mNotificationManager.createNotificationChannel(mChannel)
        }

        var intent = Intent(this, SplashActivity::class.java)
        val prefManager = Pref(this)
        if (prefManager.getUser() != null) {
            if (status == "1") {
                intent = Intent(this, MainActivity::class.java)
            }
        } else {
            intent = Intent(this, SplashActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this,
                0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, Constant.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_logo)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    application.resources,
                    R.drawable.ic_launcher_logo
                )
            )
            .setContentTitle(title)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(Notification.PRIORITY_MAX)
            .setSound(defaultSoundUri)
            .setGroup(GROUP_KEY)
            .setDeleteIntent(createOnDismissedIntent(this))
            .setContentIntent(pendingIntent)
        notificationBuilder.setContentText(msg)
        notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(msg))

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            notificationBuilder.setContentText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
//            notificationBuilder.setStyle(
//                NotificationCompat.BigTextStyle()
//                    .bigText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT))
//            );
//        } else {
//            notificationBuilder.setContentText(Html.fromHtml(msg));
//            notificationBuilder.setStyle(
//                NotificationCompat.BigTextStyle().bigText(Html.fromHtml(msg))
//            )
//        }
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_logo)
        notificationBuilder.color = resources.getColor(R.color.colorPrimary)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            Constant.CHANNEL_ID,
            System.currentTimeMillis().toInt(),
            notificationBuilder.build()
        )
    }

    private fun createOnDismissedIntent(context: Context): PendingIntent {
        val intent = Intent(context, NotificationDismissedReceiver::class.java)
        intent.putExtra("notificationId", 0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                this,
                108,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getBroadcast(this, 108, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    private fun getBitmapFromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        val TAG = MyFirebaseMessagingService::class.java.simpleName!!
    }
}