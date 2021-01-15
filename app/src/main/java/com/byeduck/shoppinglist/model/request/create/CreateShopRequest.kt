package com.byeduck.shoppinglist.model.request.create

data class CreateShopRequest(
    val shopName: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Double
)