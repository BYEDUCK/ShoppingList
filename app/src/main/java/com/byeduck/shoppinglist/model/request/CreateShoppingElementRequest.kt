package com.byeduck.shoppinglist.model.request

data class CreateShoppingElementRequest(
    val listId: Long,
    val text: String,
    val price: Double,
    val count: Int
)