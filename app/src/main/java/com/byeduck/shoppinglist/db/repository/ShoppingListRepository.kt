package com.byeduck.shoppinglist.db.repository

import androidx.lifecycle.MutableLiveData
import com.byeduck.shoppinglist.db.dao.ShoppingListDao
import com.byeduck.shoppinglist.model.view.ShoppingList

class ShoppingListRepository(private val shoppingListDao: ShoppingListDao) {

    private val allLists: MutableLiveData<List<ShoppingList>> = MutableLiveData()

    init {
        shoppingListDao.getAll().observeForever { all -> allLists.value = all.map { ShoppingList.fromModel(it) } }
    }
}