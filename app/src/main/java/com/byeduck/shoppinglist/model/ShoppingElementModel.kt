package com.byeduck.shoppinglist.model

data class ShoppingElementModel(
    val id: String = "NO-ID",
    var text: String = "NO-TXT",
    var price: Double = 0.0,
    var count: Int = 1,
    var isChecked: Boolean = false
)