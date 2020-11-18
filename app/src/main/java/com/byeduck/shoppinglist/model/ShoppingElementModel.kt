package com.byeduck.shoppinglist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_elements")
data class ShoppingElementModel(
    var listId: Long,
    var text: String,
    var price: Double,
    var count: Int = 1,
    var isChecked: Boolean = false
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    constructor(listId: Long, text: String, price: Double, count: Int, isChecked: Boolean, id: Long)
            : this(listId, text, price, count, isChecked) {
        this.id = id
    }

}