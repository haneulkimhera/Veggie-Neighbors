package com.example.veggieneighbors
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.veggieneighbors.MainActivity
import com.example.veggieneighbors.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a notification payload
        remoteMessage.notification?.let {
            sendNotification(it.title ?: "Title", it.body ?: "Message")
        }
    }

    private fun sendNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0)
        )


        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.drawable.ic_notification) // 적절한 아이콘 설정 필요
            setContentTitle(title)
            setContentText(messageBody)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
            priority = NotificationCompat.PRIORITY_MAX // 우선순위를 최대로 설정
            setDefaults(Notification.DEFAULT_ALL)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setFullScreenIntent(pendingIntent, true)
            }
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "veggie" // 채널 이름 설정 필요
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH) // 중요도를 높게 설정
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}