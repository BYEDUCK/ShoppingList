package com.byeduck.shoppinglist.model.view

data class ShoppingElement(
    val id: String,
    var text: String,
    var price: Double,
    var count: Int,
    var isChecked: Boolean
)