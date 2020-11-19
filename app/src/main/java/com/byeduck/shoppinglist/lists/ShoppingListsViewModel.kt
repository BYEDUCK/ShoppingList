package com.byeduck.shoppinglist.lists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.byeduck.shoppinglist.db.ShoppingDB
import com.byeduck.shoppinglist.db.repository.ShoppingListRepository
import com.byeduck.shoppinglist.model.request.CreateShoppingListRequest
import com.byeduck.shoppinglist.model.view.ShoppingList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ShoppingListRepository
    val shoppingLists: LiveData<List<ShoppingList>>

    init {
        val shoppingListDao = ShoppingDB.getDatabase(application).shoppingListDao()
        repository = ShoppingListRepository(shoppingListDao)
        shoppingLists = repository.allLists
    }

    fun addShoppingList(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(CreateShoppingListRequest(name))
        }
    }

    fun deleteShoppingListById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteById(id)
        }
    }

    fun editShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.edit(shoppingList)
        }
    }

}