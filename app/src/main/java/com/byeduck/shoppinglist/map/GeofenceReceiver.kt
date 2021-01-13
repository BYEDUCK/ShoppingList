package com.byeduck.shoppinglist.map

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.byeduck.shoppinglist.lists.ShoppingListsActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geoEvent = GeofencingEvent.fromIntent(intent)
        val triggeringGeofences = geoEvent.triggeringGeofences
        for (geofence in triggeringGeofences) {
            Log.d("GEOFENCE TRIGGERED", "id = ${geofence.requestId}")
            val shopId = geofence.requestId.takeWhile { it != '-' }

            val userActivity: String = when (geoEvent.geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> "enter"
                Geofence.GEOFENCE_TRANSITION_EXIT -> "exit"
                else -> {
                    Log.e("GEOFENCE", "ERROR")
                    return
                }
            }

            val activityIntent = Intent(context, ShoppingListsActivity::class.java).apply {
                putExtra("shopId", shopId)
            }
            val pendingIntent = PendingIntent.getActivity(
                context, System.currentTimeMillis().toInt(), activityIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val notification = NotificationGenerator.getNotification(
                userActivity, shopId, pendingIntent, context
            )
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }
}