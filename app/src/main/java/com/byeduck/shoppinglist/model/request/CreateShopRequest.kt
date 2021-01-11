package com.byeduck.shoppinglist.model.request

data class CreateShopRequest(
    val shopName: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)