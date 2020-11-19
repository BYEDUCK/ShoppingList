package com.byeduck.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.byeduck.shoppinglist.model.ShoppingElementModel

@Dao
interface ShoppingElementDao {

    @Insert
    fun insert(shoppingElement: ShoppingElementModel)

    @Delete
    fun delete(shoppingElement: ShoppingElementModel)

    @Query("UPDATE list_elements SET isChecked=:isChecked WHERE id=:id")
    fun setCheckedById(isChecked: Boolean, id: Long)

}