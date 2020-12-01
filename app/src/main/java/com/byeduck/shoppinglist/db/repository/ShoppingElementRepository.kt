package com.byeduck.shoppinglist.db.repository

import com.byeduck.shoppinglist.db.dao.ShoppingElementDao
import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.request.CreateShoppingElementRequest
import com.byeduck.shoppinglist.model.view.ShoppingElement

class ShoppingElementRepository(private val shoppingElementDao: ShoppingElementDao) {

    suspend fun insert(request: CreateShoppingElementRequest) = shoppingElementDao.insert(
        ShoppingElementModel(request.listId, request.text, request.price, request.count)
    )

    suspend fun deleteById(elementId: Long) = shoppingElementDao.deleteById(elementId)

    suspend fun setIsCheckedById(isChecked: Boolean, id: Long) =
        shoppingElementDao.setCheckedById(isChecked, id)

    suspend fun update(shoppingElement: ShoppingElement) = shoppingElementDao.update(
        ShoppingElementModel(
            shoppingElement.listId, shoppingElement.text, shoppingElement.price,
            shoppingElement.count, shoppingElement.isChecked, shoppingElement.id
        )
    )

    suspend fun getById(elementId: Long) =
        ShoppingElement.fromModel(shoppingElementDao.getById(elementId))
}