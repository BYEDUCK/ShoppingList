package com.byeduck.shoppinglist.lists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.byeduck.shoppinglist.db.ShoppingDB
import com.byeduck.shoppinglist.db.repository.ShoppingListRepository
import com.byeduck.shoppinglist.model.request.CreateShoppingListRequest
import com.byeduck.shoppinglist.model.view.ShoppingListWithElements
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingListsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ShoppingListRepository
    val shoppingLists: LiveData<List<ShoppingListWithElements>>

    init {
        val shoppingListDao = ShoppingDB.getDatabase(application).shoppingListDao()
        repository = ShoppingListRepository(shoppingListDao)
        shoppingLists = repository.allLists
        viewModelScope.launch {
            insertMockData()
        }
    }

    private suspend fun insertMockData() {
        withContext(Dispatchers.IO) {
            repository.insert(CreateShoppingListRequest("First shopping list"))
            repository.insert(CreateShoppingListRequest("Second shopping list"))
            repository.insert(CreateShoppingListRequest("Third shopping list"))
        }
    }

}