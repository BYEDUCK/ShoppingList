package com.byeduck.shoppinglist.util

import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.byeduck.shoppinglist.model.view.ShoppingList
import com.google.gson.Gson
import java.util.*

sealed class ShoppingListConverter {

    companion object {

        fun listFromModelMap(map: Map<*, *>): ShoppingList {
            val gson = Gson()
            val json = gson.toJson(map)
            val model = gson.fromJson(json, ShoppingListModel::class.java)
            return listFromModel(model)
        }

        private fun listFromModel(shoppingList: ShoppingListModel) = ShoppingList(
            shoppingList.id,
            shoppingList.name,
            Date(shoppingList.createdAt),
            Date(shoppingList.updatedAt),
            shoppingList.elements.map { listElemFromModel(it) }
        )

        private fun listElemFromModel(elem: ShoppingElementModel) = ShoppingElement(
            elem.id, elem.text, elem.price, elem.count, elem.isChecked
        )

    }

}