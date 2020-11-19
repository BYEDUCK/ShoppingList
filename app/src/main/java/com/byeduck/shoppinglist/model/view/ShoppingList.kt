package com.byeduck.shoppinglist.model.view

import com.byeduck.shoppinglist.model.ShoppingListModel
import java.util.*

data class ShoppingList(
    val id: Long,
    var name: String,
    var createdAt: Date,
    var updatedAt: Date
) {

    companion object {
        fun fromModel(model: ShoppingListModel) = ShoppingList(
            model.id, model.name, model.createdAt, model.updatedAt
        )
    }

}