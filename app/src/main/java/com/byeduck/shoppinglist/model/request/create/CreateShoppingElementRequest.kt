package com.byeduck.shoppinglist.model.request.create

data class CreateShoppingElementRequest(
    val listId: String,
    val text: String,
    val price: Double,
    val count: Int
)