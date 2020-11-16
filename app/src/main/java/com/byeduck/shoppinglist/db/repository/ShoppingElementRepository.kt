package com.byeduck.shoppinglist.db.repository

import com.byeduck.shoppinglist.db.dao.ShoppingElementDao
import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.request.CreateShoppingElementRequest

class ShoppingElementRepository(private val shoppingElementDao: ShoppingElementDao) {

    fun insert(request: CreateShoppingElementRequest) = shoppingElementDao.insert(
        ShoppingElementModel(request.listId, request.text, request.price)
    )

}