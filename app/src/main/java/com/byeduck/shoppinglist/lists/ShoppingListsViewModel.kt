package com.byeduck.shoppinglist.lists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.byeduck.shoppinglist.model.request.CreateShoppingListRequest
import com.byeduck.shoppinglist.repository.ShoppingListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListsViewModel(application: Application) : AndroidViewModel(application) {


    fun getDbRef() = ShoppingListRepository.getDbRef()

    fun addShoppingList(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            ShoppingListRepository.insert(CreateShoppingListRequest(name))
        }
    }

    fun deleteShoppingListById(id: String) = ShoppingListRepository.deleteById(id)

//    fun editShoppingList(shoppingList: ShoppingList) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.edit(shoppingList)
//        }
//    }

}