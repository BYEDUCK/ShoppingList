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
            shoppingList.updatedAt.time
        )

        fun listFromModel(shoppingList: ShoppingListModel) = ShoppingList(
            shoppingList.id,
            shoppingList.name,
            Date(shoppingList.createdAt),
            Date(shoppingList.updatedAt)
        )

        fun modelFromElem(elem: ShoppingElement) = ShoppingElementModel(
            elem.id, elem.text, elem.price, elem.count, elem.isChecked
        )

        fun elemFromModel(model: ShoppingElementModel) = ShoppingElement(
            model.id, model.text, model.price, model.count, model.isChecked
        )

    }

}