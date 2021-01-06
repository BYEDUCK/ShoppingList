package com.byeduck.shoppinglist.map

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

class GoogleMapsService(
    private val apiKey: String
) {

    private val baseUrl = "https://maps.googleapis.com/maps/api/"
    private val searchRadiusMeters = 50;

    private val googleMapsAPI: GoogleMapsAPI;

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        googleMapsAPI = retrofit.create()
    }


    suspend fun getShopInLocation(latLng: LatLng): String {
        return withContext(Dispatchers.IO) {
            Log.d("SERVICE", "Api call: $apiKey")
            val res = googleMapsAPI.findShop(
                apiKey,
                "name",
                "circle:${searchRadiusMeters}@${latLng.latitude},${latLng.longitude}"
            ).awaitResponse()
            Log.d("RESPONSE", "SUCCESS: ${res.isSuccessful}")
            if (!res.isSuccessful) {
                throw RuntimeException("Request not successful: ${res.code()}")
            }
            return@withContext res.body() ?: throw RuntimeException("No body")
        }
    }

}