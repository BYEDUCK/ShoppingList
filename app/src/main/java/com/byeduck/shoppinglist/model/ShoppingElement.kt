package com.byeduck.shoppinglist.model

data class ShoppingElement(
    val id: Long,
    val listId: Long,
    var text: String,
    var price: Double,
    var count: Int,
    var isChecked: Boolean
) {

    companion object {
        fun fromModel(model: ShoppingElementModel) = ShoppingElement(
            model.id, model.listId, model.text, model.price, model.count, model.isChecked
        )
    }

}