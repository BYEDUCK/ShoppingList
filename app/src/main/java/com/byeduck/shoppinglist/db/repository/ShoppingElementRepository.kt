package com.byeduck.shoppinglist.db.repository

import com.byeduck.shoppinglist.db.dao.ShoppingElementDao
import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.request.CreateShoppingElementRequest
import com.byeduck.shoppinglist.model.view.ShoppingElement

class ShoppingElementRepository(private val shoppingElementDao: ShoppingElementDao) {

    fun insert(request: CreateShoppingElementRequest) = shoppingElementDao.insert(
        ShoppingElementModel(request.listId, request.text, request.price)
    )

    fun remove(shoppingElement: ShoppingElement) = shoppingElementDao.delete(
        ShoppingElementModel(
            shoppingElement.listId, shoppingElement.text, shoppingElement.price,
            shoppingElement.count, shoppingElement.isChecked, shoppingElement.id
        )
    )

    fun setIsCheckedById(isChecked: Boolean, id: Long) =
        shoppingElementDao.setCheckedById(isChecked, id)

}