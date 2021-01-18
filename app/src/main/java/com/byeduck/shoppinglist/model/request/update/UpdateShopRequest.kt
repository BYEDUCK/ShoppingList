package com.byeduck.shoppinglist.model.request.update

data class UpdateShopRequest(
    val shopId: String,
    val shopName: String,
    val shopDescription: String,
    val radius: Double,
    val isFavourite: Boolean
)