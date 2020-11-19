package com.byeduck.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.byeduck.shoppinglist.model.ShoppingElementModel

@Dao
interface ShoppingElementDao {

    @Insert
    fun insert(shoppingElement: ShoppingElementModel)

    @Query("DELETE FROM list_elements WHERE id=:id")
    fun deleteById(id: Long)

    @Query("UPDATE list_elements SET isChecked=:isChecked WHERE id=:id")
    fun setCheckedById(isChecked: Boolean, id: Long)

    @Update
    fun update(shoppingElement: ShoppingElementModel)

}