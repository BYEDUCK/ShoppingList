package com.byeduck.shoppinglist.model.view

import com.byeduck.shoppinglist.model.ShoppingListModel
import java.util.*

data class ShoppingList(
    val id: Long,
    val elements: List<ShoppingElement>,
    val createdAt: Date,
    var updatedAt: Date,
    var name: String
) {
    companion object {
        fun fromModel(model: ShoppingListModel) = ShoppingList(
            model.id, model.listElements.map { ShoppingElement.fromModel(it) }, model.createdAt, model.updatedAt, model.name
        )
    }
}