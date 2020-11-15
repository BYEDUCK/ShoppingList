package com.byeduck.shoppinglist.model.view

import com.byeduck.shoppinglist.model.ShoppingListWithElementsModel
import java.util.*

data class ShoppingListWithElements(
    val listId: Long,
    val elements: List<ShoppingElement>,
    val createdAt: Date,
    var updatedAt: Date,
    var listName: String
) {
    companion object {
        fun fromModel(model: ShoppingListWithElementsModel) = ShoppingListWithElements(
            model.shoppingList.id, model.elementList.map { ShoppingElement.fromModel(it) },
            model.shoppingList.createdAt, model.shoppingList.updatedAt, model.shoppingList.name
        )
    }
}