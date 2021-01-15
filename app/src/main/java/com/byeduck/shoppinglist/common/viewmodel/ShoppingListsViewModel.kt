package com.byeduck.shoppinglist.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.byeduck.shoppinglist.model.request.create.CreateShoppingElementRequest
import com.byeduck.shoppinglist.model.request.create.CreateShoppingListRequest
import com.byeduck.shoppinglist.model.request.update.UpdateShoppingListRequest
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.byeduck.shoppinglist.repository.ShoppingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShoppingListsViewModel(application: Application) : AndroidViewModel(application) {


    fun getDbListsRef() = ShoppingRepository.getDbListsRef()

    fun getDbListElemRef(listId: String) = ShoppingRepository.getDbListElemRef(listId)

    fun addShoppingList(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            ShoppingRepository.insertList(CreateShoppingListRequest(name))
        }
    }

    fun addShoppingListElementAsync(listId: String, text: String, price: Double, count: Int) =
        viewModelScope.async(Dispatchers.IO) {
            return@async ShoppingRepository.insertListElement(
                CreateShoppingElementRequest(
                    listId, text, price, count
                )
            )
        }

    fun deleteShoppingListById(id: String) = ShoppingRepository.deleteListById(id)

    fun deleteElemById(listId: String, elemId: String) =
        ShoppingRepository.deleteListElementById(listId, elemId)

    fun updateShoppingList(listId: String, listName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            ShoppingRepository.updateList(UpdateShoppingListRequest(listId, listName))
        }
    }

    fun updateShoppingElem(listId: String, elem: ShoppingElement) {
        viewModelScope.launch(Dispatchers.IO) {
            ShoppingRepository.updateElem(listId, elem)
        }
    }

}