package com.byeduck.shoppinglist.map

import com.google.android.gms.maps.model.LatLng

data class ShopMarker(
    val shopName: String,
    val location: LatLng,
    val radius: Double
)