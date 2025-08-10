package com.app.delmon.fcm

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.activity.MainActivity
import com.app.delmon.activity.SplashScreenActivity
import com.app.delmon.app.AppController
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private  var sharedHelper: SharedHelper = SharedHelper(this)

    override fun onNewToken(token: String) {
        // TODO: send token to your backend for mapping with user/session
        Log.d("FCM", "New token: $token")
        sharedHelper.fcmToken = token
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // If message has 'notification' payload, Firebase may auto-display (background).
        // We handle foreground + data payloads here.
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "New message"
        val body  = remoteMessage.notification?.body  ?: remoteMessage.data["body"]  ?: ""

        showNotification(title, body, remoteMessage.data)
    }

    private fun showNotification(title: String, body: String, data: Map<String, String>) {
        AppController.NotifyChannels.ensure(this)

        val intent = Intent(this, SplashScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            // Pass data for deep link handling
            data["deeplink"]?.let { putExtra("deeplink", it) }
        }
        val pi = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notif = NotificationCompat.Builder(this, AppController.NotifyChannels.GENERAL)
            .setSmallIcon(R.drawable.delmon_short_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(pi)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(this).notify(System.currentTimeMillis().toInt(), notif)
    }
}
