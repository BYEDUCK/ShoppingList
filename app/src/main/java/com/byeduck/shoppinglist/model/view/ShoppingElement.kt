package com.byeduck.shoppinglist.model.view

import com.byeduck.shoppinglist.model.ShoppingElementModel

data class ShoppingElement(
    val id: String,
    var text: String,
    var price: Double,
    var count: Int,
    var isChecked: Boolean
) {

    companion object {
        fun fromModel(model: ShoppingElementModel) = ShoppingElement(
            model.id, model.text, model.price, model.count, model.isChecked
        )
    }

}