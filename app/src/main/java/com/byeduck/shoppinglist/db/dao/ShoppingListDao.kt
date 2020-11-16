package com.byeduck.shoppinglist.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.ShoppingListWithElementsModel

@Dao
interface ShoppingListDao {

    @Transaction
    @Query("SELECT * FROM shopping_lists")
    fun getAll(): LiveData<List<ShoppingListWithElementsModel>>

    @Transaction
    @Query("SELECT * FROM shopping_lists WHERE id=:id")
    fun getById(id: Long): LiveData<ShoppingListWithElementsModel>

    @Insert
    fun insert(shoppingList: ShoppingListModel)

}