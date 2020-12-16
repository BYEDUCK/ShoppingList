package com.byeduck.shoppinglist.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.byeduck.shoppinglist.model.request.CreateShoppingElementRequest
import com.byeduck.shoppinglist.model.request.CreateShoppingListRequest
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.byeduck.shoppinglist.model.view.ShoppingList
import com.byeduck.shoppinglist.repository.ShoppingListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShoppingListsViewModel(application: Application) : AndroidViewModel(application) {


    fun getDbListsRef() = ShoppingListRepository.getDbListsRef()

    fun getDbListElemRef(listId: String) = ShoppingListRepository.getDbListElemRef(listId)

    fun addShoppingList(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            ShoppingListRepository.insertList(CreateShoppingListRequest(name))
        }
    }

    fun addShoppingListElementAsync(listId: String, text: String, price: Double, count: Int) =
        viewModelScope.async(Dispatchers.IO) {
            return@async ShoppingListRepository.insertListElement(
                CreateShoppingElementRequest(
                    listId, text, price, count
                )
            )
        }

    fun deleteShoppingListById(id: String) = ShoppingListRepository.deleteListById(id)

    fun deleteElemById(listId: String, elemId: String) =
        ShoppingListRepository.deleteListElementById(listId, elemId)

    fun updateShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch(Dispatchers.IO) {
            ShoppingListRepository.updateList(shoppingList)
        }
    }

    fun updateShoppingElem(listId: String, elem: ShoppingElement) {
        viewModelScope.launch(Dispatchers.IO) {
            ShoppingListRepository.updateElem(listId, elem)
        }
    }

}