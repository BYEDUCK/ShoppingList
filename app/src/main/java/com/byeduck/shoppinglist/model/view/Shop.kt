package com.byeduck.shoppinglist.model.view

data class Shop(
    var id: String,
    var name: String,
    var description: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Double
)