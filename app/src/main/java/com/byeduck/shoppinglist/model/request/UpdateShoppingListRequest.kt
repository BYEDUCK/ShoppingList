package com.byeduck.shoppinglist.model.request

data class UpdateShoppingListRequest(
    val listId: String,
    val listName: String
)