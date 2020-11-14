package com.byeduck.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.byeduck.shoppinglist.model.ShoppingListModel

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_lists")
    fun getAll(): List<ShoppingListModel>

    @Insert
    fun insert(shoppingList: ShoppingListModel)

}