package com.byeduck.shoppinglist.model.request

data class UpdateShopRequest(
    val shopId: String,
    val shopName: String,
    val shopDescription: String,
    val radius: Double
)