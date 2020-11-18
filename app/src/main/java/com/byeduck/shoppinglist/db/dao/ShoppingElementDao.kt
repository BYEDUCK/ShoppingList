package com.byeduck.shoppinglist.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.byeduck.shoppinglist.model.ShoppingElementModel

@Dao
interface ShoppingElementDao {

    @Query("SELECT * FROM list_elements WHERE listId = :listId")
    fun getForListId(listId: Long): LiveData<List<ShoppingElementModel>>

    @Insert
    fun insert(shoppingElement: ShoppingElementModel)

    @Delete
    fun delete(shoppingElement: ShoppingElementModel)

}