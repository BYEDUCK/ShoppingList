package com.byeduck.shoppinglist.db.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.ShoppingListWithElementsModel

@Dao
interface ShoppingListDao {

    @Transaction
    @Query("SELECT * FROM shopping_lists")
    fun getAll(): LiveData<List<ShoppingListModel>>

    @Transaction
    @Query("SELECT * FROM shopping_lists")
    fun getAllWithElements(): Cursor

    @Transaction
    @Query("SELECT * FROM shopping_lists WHERE id=:id")
    fun getByIdWithElements(id: Long): LiveData<ShoppingListWithElementsModel>

    @Insert
    suspend fun insert(shoppingList: ShoppingListModel)

    @Transaction
    @Query("DELETE FROM shopping_lists WHERE id=:id")
    suspend fun deleteById(id: Long)

    @Update
    suspend fun update(shoppingListModel: ShoppingListModel)

}