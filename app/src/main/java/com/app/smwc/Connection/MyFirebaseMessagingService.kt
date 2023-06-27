package com.app.smwc.Connection

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
import android.text.Html
import androidx.core.app.NotificationCompat
import com.app.smwc.Activity.MainActivity
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.R
import com.app.ssn.Common.Pref
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {
    var GROUP_KEY = "com.app.smwc.user.GROUP"
    private var catName: String? = null
    private val prefs: Pref? = null

    @Override
    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }

    @Override
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        showNotification(
            remoteMessage.data["title"],
            remoteMessage.data["msg"],
            remoteMessage.data["flag"], remoteMessage.data["image"]
        )
        HELPER.print("NotificationsResponse::", Gson().toJson(remoteMessage))
    }

    private fun showNotification(
        title: String?,
        msg: String?,
        message: String?,
        url: String?,
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
        val copiedMessage = message
        val intent = Intent(this, MainActivity::class.java)
//        PreafManager preafManager = new PreafManager(this);
//        if (preafManager.getUserToken() != null && !preafManager.getUserToken().isEmpty()) {
//            if (copiedMessage != null && copiedMessage.equalsIgnoreCase("addBrand")) {
//                intent = new Intent(this, ViewBrandActivity.class);
//            } else if (copiedMessage != null && copiedMessage.equalsIgnoreCase("addFrame")) {
//                intent = new Intent(this, ViewBrandActivity.class);
//            } else {
//                if (cat_id == null || cat_id.equals("0"))
//                    intent = new Intent(this, HomeActivity.class);
//                else {
//                    intent = new Intent(this, ImageCategoryDetailActivity.class);
//                    intent.putExtra("notification", "1");
//                    intent.putExtra("cat_id", cat_id);
//                    intent.putExtra("catName", catName);
//                }
//            }
//        } else {
//            intent = new Intent(this, SpleshActivity.class);
//        }
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
            .setSmallIcon(R.drawable.notification_icon)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    application.resources,
                    R.drawable.notification_icon
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setContentText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
            notificationBuilder.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT))
            );
        } else {
            notificationBuilder.setContentText(Html.fromHtml(msg));
            notificationBuilder.setStyle(
                NotificationCompat.BigTextStyle().bigText(Html.fromHtml(msg))
            );
        }

        if (url != null && url.isNotEmpty()) {
            val bitmap: Bitmap = getBitmapFromUrl(url)!!
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(bitmap)
            ).setLargeIcon(bitmap)
        }
        notificationBuilder.setSmallIcon(R.drawable.notification_icon)
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