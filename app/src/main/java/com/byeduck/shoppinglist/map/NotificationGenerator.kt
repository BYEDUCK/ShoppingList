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
            val title = if (promo == null) {
                "You ${userActivity}ed shop"
            } else {
                "You ${userActivity}ed shop ${promo.shopName}"
            }
            val text = if (promo == null) {
                "Sorry - no promotion for today"
            } else {
                "Promotion for today is ${promo.name}\n${promo.shortDescription}"
            }
            return NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
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