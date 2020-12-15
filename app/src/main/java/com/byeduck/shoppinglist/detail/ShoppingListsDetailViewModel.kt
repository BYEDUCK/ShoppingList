package com.byeduck.shoppinglist.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.byeduck.shoppinglist.model.view.ShoppingElement

class ShoppingListsDetailViewModel(application: Application, private val listId: Long) :
    AndroidViewModel(application) {


    fun addShoppingElement(text: String, price: Double, count: Int = 1) = 1L

    fun deleteShoppingElementById(elementId: Long) {
        TODO()
    }

    fun checkShoppingElementById(isChecked: Boolean, id: Long) {
        TODO()
    }

    fun updateShoppingElement(shoppingElement: ShoppingElement) {
        TODO()
    }

    fun getShoppingElementById(elementId: Long) = ShoppingElement("1", "123", 1.0, 1, false)

}