package com.byeduck.shoppinglist.db.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.byeduck.shoppinglist.db.dao.ShoppingListDao
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.request.CreateShoppingListRequest
import com.byeduck.shoppinglist.model.view.ShoppingList
import com.byeduck.shoppinglist.model.view.ShoppingListWithElements
import java.util.*

class ShoppingListRepository(private val shoppingListDao: ShoppingListDao) {

    val allLists: LiveData<List<ShoppingList>> =
        Transformations.map(shoppingListDao.getAll()) { lists ->
            lists.map { ShoppingList.fromModel(it) }
        }

    fun insert(request: CreateShoppingListRequest) {
        shoppingListDao.insert(ShoppingListModel(request.name))
    }

    fun getById(id: Long): LiveData<ShoppingListWithElements> {
        val model = shoppingListDao.getByIdWithElements(id)
        return Transformations.map(model) { list ->
            list?.let {
                ShoppingListWithElements.fromModel(it)
            }
        }
    }

    fun deleteById(id: Long) = shoppingListDao.deleteById(id)

    fun edit(shoppingList: ShoppingList) = shoppingListDao.update(
        ShoppingListModel(
            shoppingList.id, shoppingList.name, Date(), shoppingList.createdAt
        )
    )
}