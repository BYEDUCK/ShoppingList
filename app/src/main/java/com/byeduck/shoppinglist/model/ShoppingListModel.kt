package com.byeduck.shoppinglist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "shopping_lists")
data class ShoppingListModel(
    var name: String,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date()
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    constructor(id: Long, name: String, updatedAt: Date, createdAt: Date) : this(
        name,
        createdAt,
        updatedAt
    ) {
        this.id = id
    }

}