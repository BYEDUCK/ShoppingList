package com.byeduck.shoppinglist.map

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.byeduck.shoppinglist.R
import java.util.*

class NotificationGenerator {

    companion object {

        fun getNotification(
            userActivity: String,
            shopId: String,
            pendingIntent: PendingIntent,
            context: Context
        ): Notification {
            val channelId = createNotificationChannel(context)
            return NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Shop -> $userActivity")
                .setContentText("You did ${userActivity}ed shop with id $shopId")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        }

        private fun createNotificationChannel(context: Context): String {
            val channelId = UUID.randomUUID().toString()
            val notificationChannel = NotificationChannel(
                channelId,
                "Geofence_Noti",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "Geofence Notifications"
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.createNotificationChannel(notificationChannel)
            return channelId
        }

    }
}