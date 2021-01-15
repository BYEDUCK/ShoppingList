package com.byeduck.shoppinglist.model.request.update

data class UpdateShoppingListRequest(
    val listId: String,
    val listName: String
)