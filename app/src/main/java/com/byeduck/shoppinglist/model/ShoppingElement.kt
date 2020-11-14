package com.byeduck.shoppinglist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_elements")
data class ShoppingElement(
    var listId: Long,
    var text: String,
    var price: Double,
    var count: Int = 1,
    var isChecked: Boolean = false
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

}