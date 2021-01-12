package com.byeduck.shoppinglist.map

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geoEvent = GeofencingEvent.fromIntent(intent)
        val triggeringGeofences = geoEvent.triggeringGeofences
        for (geofence in triggeringGeofences) {
            Log.d("GEOFENCE TRIGGERED", "id = ${geofence.requestId}")
        }
        // TODO: send notification
        when (geoEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> Log.d(
                "GEOFENCE",
                "ENTER: ${geoEvent.triggeringLocation}"
            )
            Geofence.GEOFENCE_TRANSITION_EXIT -> Log.d(
                "GEOFENCE",
                "EXIT: ${geoEvent.triggeringLocation}"
            )
            else -> Log.e("GEOFENCE", "ERROR")
        }
    }
}