package com.byeduck.shoppinglist.db.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.byeduck.shoppinglist.db.dao.ShoppingListDao
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.request.CreateShoppingListRequest
import com.byeduck.shoppinglist.model.view.ShoppingListWithElements

class ShoppingListRepository(private val shoppingListDao: ShoppingListDao) {

    val allLists: LiveData<List<ShoppingListWithElements>> =
        Transformations.map(shoppingListDao.getAll()) { lists ->
            lists.map { ShoppingListWithElements.fromModel(it) }
        }

    fun insert(request: CreateShoppingListRequest) {
        shoppingListDao.insert(ShoppingListModel(request.name))
    }

    fun getById(id: Long): LiveData<ShoppingListWithElements> {
        val model = shoppingListDao.getById(id)
        return Transformations.map(model) { ShoppingListWithElements.fromModel(it) }
    }

    fun remove(shoppingListWithElements: ShoppingListWithElements) {
        shoppingListDao.delete(
            ShoppingListModel(
                shoppingListWithElements.listName,
                shoppingListWithElements.createdAt,
                shoppingListWithElements.updatedAt,
                shoppingListWithElements.listId
            )
        )
    }
}