package com.byeduck.shoppinglist.util

import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.byeduck.shoppinglist.model.view.ShoppingList
import java.util.*

sealed class ShoppingListConverter {

    companion object {

        fun modelFromList(shoppingList: ShoppingList) = ShoppingListModel(
            shoppingList.id,
            shoppingList.name,
            shoppingList.createdAt.time,
            shoppingList.updatedAt.time,
            shoppingList.elements.map { modelFromListElem(it) }
        )

        fun listFromModel(shoppingList: ShoppingListModel) = ShoppingList(
            shoppingList.id,
            shoppingList.name,
            Date(shoppingList.createdAt),
            Date(shoppingList.updatedAt),
            shoppingList.elements.map { listElemFromModel(it) }
        )

        private fun listElemFromModel(elem: ShoppingElementModel) = ShoppingElement(
            elem.id, elem.text, elem.price, elem.count, elem.isChecked
        )

        private fun modelFromListElem(elem: ShoppingElement) = ShoppingElementModel(
            elem.id, elem.text, elem.price, elem.count, elem.isChecked
        )

    }

}