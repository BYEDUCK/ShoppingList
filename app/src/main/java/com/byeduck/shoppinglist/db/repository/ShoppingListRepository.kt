package com.byeduck.shoppinglist.db.repository

import androidx.lifecycle.MutableLiveData
import com.byeduck.shoppinglist.db.dao.ShoppingListDao
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.request.CreateShoppingListRequest
import com.byeduck.shoppinglist.model.view.ShoppingListWithElements

class ShoppingListRepository(private val shoppingListDao: ShoppingListDao) {

    val allLists: MutableLiveData<List<ShoppingListWithElements>> = MutableLiveData()

    init {
        shoppingListDao.getAll().observeForever { all ->
            allLists.value = all.map { ShoppingListWithElements.fromModel(it) }
        }
    }

    fun insert(request: CreateShoppingListRequest) {
        shoppingListDao.insert(ShoppingListModel(request.name))
    }
}