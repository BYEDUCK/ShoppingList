package com.byeduck.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.byeduck.shoppinglist.model.ShoppingElementModel

@Dao
interface ShoppingElementDao {

    @Insert
    suspend fun insert(shoppingElement: ShoppingElementModel): Long

    @Query("DELETE FROM list_elements WHERE id=:id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE list_elements SET isChecked=:isChecked WHERE id=:id")
    suspend fun setCheckedById(isChecked: Boolean, id: Long)

    @Query("SELECT * FROM list_elements WHERE id=:id")
    suspend fun getById(id: Long): ShoppingElementModel

    @Update
    suspend fun update(shoppingElement: ShoppingElementModel)

}