package com.byeduck.shoppinglist.map

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.model.view.Promotion
import java.util.*

class NotificationGenerator {

    companion object {

        fun getNotification(
            userActivity: String,
            promo: Promotion?,
            pendingIntent: PendingIntent,
            context: Context
        ): Notification {
            val channelId = createNotificationChannel(context)
            return if (promo == null)
                NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("You ${userActivity}ed shop")
                    .setContentText("Sorry - no promotion for today")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()
            else
                NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("You ${userActivity}ed shop ${promo.shopName}")
                    .setContentText("Promotion for today is ${promo.name}\n${promo.shortDescription}")
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