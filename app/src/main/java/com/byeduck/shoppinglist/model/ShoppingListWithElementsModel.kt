package com.byeduck.shoppinglist.model

import androidx.room.Embedded
import androidx.room.Relation

data class ShoppingListWithElementsModel(
    @Embedded val shoppingList: ShoppingListModel
) {
    @Relation(parentColumn = "id", entityColumn = "listId")
    var elementList: List<ShoppingElementModel> = emptyList()
}