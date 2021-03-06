package com.byeduck.shoppinglist.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.byeduck.shoppinglist.db.ShoppingDB
import com.byeduck.shoppinglist.db.repository.ShoppingElementRepository
import com.byeduck.shoppinglist.db.repository.ShoppingListRepository
import com.byeduck.shoppinglist.model.request.CreateShoppingElementRequest
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.byeduck.shoppinglist.model.view.ShoppingListWithElements
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShoppingListsDetailViewModel(application: Application, private val listId: Long) :
    AndroidViewModel(application) {

    private val listRepository: ShoppingListRepository
    private val elementRepository: ShoppingElementRepository
    val shoppingList: LiveData<ShoppingListWithElements>

    init {
        val shoppingListDao = ShoppingDB.getDatabase(application).shoppingListDao()
        listRepository = ShoppingListRepository(shoppingListDao)
        val shoppingElementDao = ShoppingDB.getDatabase(application).shoppingElementDao()
        elementRepository = ShoppingElementRepository(shoppingElementDao)
        shoppingList = listRepository.getById(listId)
    }

    fun addShoppingElement(text: String, price: Double, count: Int = 1) =
        runBlocking(Dispatchers.IO) {
            elementRepository.insert(CreateShoppingElementRequest(listId, text, price, count))
        }

    fun deleteShoppingElementById(elementId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            elementRepository.deleteById(elementId)
        }
    }

    fun checkShoppingElementById(isChecked: Boolean, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            elementRepository.setIsCheckedById(isChecked, id)
        }
    }

    fun updateShoppingElement(shoppingElement: ShoppingElement) {
        viewModelScope.launch(Dispatchers.IO) {
            elementRepository.update(shoppingElement)
        }
    }

    fun getShoppingElementById(elementId: Long) = runBlocking(Dispatchers.IO) {
        elementRepository.getById(elementId)
    }

}