package com.byeduck.shoppinglist.model.view

import com.byeduck.shoppinglist.model.ShoppingListModel
import java.util.*

data class ShoppingList(
    val id: Long,
    val name: String,
    val createdAt: Date,
    val updatedAt: Date
) {

    companion object {
        fun fromModel(model: ShoppingListModel) = ShoppingList(
            model.id, model.name, model.createdAt, model.updatedAt
        )
    }

}