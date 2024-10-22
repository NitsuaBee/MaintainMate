package com.example.maintainmate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

fun showNotification(context: Context, title: String, message: String) {
    val channelId = "MaintainMateChannel"

    // Create notification channel (required for Android O and above)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "MaintainMate"
        val descriptionText = "MaintainMate Notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Build the notification
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_notification) // Make sure you have a drawable resource
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    // Show the notification
    with(NotificationManagerCompat.from(context)) {
        notify(0, builder.build())
    }
}
