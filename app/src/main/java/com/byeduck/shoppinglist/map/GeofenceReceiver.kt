package com.byeduck.shoppinglist.map

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.byeduck.shoppinglist.MainActivity
import com.byeduck.shoppinglist.common.converter.ShopConverter
import com.byeduck.shoppinglist.model.PromotionModel
import com.byeduck.shoppinglist.model.view.Promotion
import com.byeduck.shoppinglist.repository.ShoppingRepository
import com.byeduck.shoppinglist.shops.promo.AddEditViewPromotionActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.util.*

class GeofenceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("GEOFENCE RECEIVER", "BROADCAST RECEIVED")
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
            val promoId = Objects.hash(shopId, LocalDate.now().toString()).toString(16)
            Log.d("GEOFENCE RECEIVER", "PROMO ID: $promoId for today ${LocalDate.now()}")
            ShoppingRepository.getDbPromosRef(shopId)
                .child(promoId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val notificationManager = NotificationManagerCompat.from(context)
                        var promo: Promotion? = null
                        val activityIntent = if (snapshot.exists()) {
                            val promoModel = snapshot.getValue(PromotionModel::class.java) ?: return
                            promo = ShopConverter.promotionFromModel(promoModel)
                            Intent(context, AddEditViewPromotionActivity::class.java).apply {
                                putExtra("promoId", promoId)
                                putExtra("view", true)
                            }
                        } else {
                            Intent(context, MainActivity::class.java)
                        }
                        val pendingIntent = PendingIntent.getActivity(
                            context, System.currentTimeMillis().toInt(), activityIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                        )
                        val notification = NotificationGenerator.getNotification(
                            userActivity, promo, pendingIntent, context
                        )
                        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("GEOFENCE RECEIVER", "ERROR", error.toException())
                    }

                })
        }
    }
}