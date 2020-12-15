package com.byeduck.shoppinglist.model

import com.byeduck.shoppinglist.util.ShoppingListJsonAdapter
import com.google.gson.annotations.JsonAdapter
import java.util.*

@JsonAdapter(ShoppingListJsonAdapter::class)
data class ShoppingListModel(
    val id: String,
    var name: String,
    var createdAt: Long = Date().time,
    var updatedAt: Long = Date().time,
    var elements: List<ShoppingElementModel> = emptyList()
)