package com.byeduck.shoppinglist.model

data class ShoppingElementModel(
    val id: String,
    var text: String,
    var price: Double,
    var count: Int = 1,
    var isChecked: Boolean = false
)