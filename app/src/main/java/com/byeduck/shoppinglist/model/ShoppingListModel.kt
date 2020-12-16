package com.byeduck.shoppinglist.model

import java.util.*

data class ShoppingListModel(
    val id: String = "NO-ID",
    var name: String = "NO-NAME",
    var createdAt: Long = Date().time,
    var updatedAt: Long = Date().time,
    var elements: List<ShoppingElementModel> = emptyList()
)