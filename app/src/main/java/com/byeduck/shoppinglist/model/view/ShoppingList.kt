package com.byeduck.shoppinglist.model.view

import java.util.*

data class ShoppingList(
    val id: String,
    var name: String,
    var createdAt: Date,
    var updatedAt: Date
)