package com.byeduck.shoppinglist.repository

import com.byeduck.shoppinglist.common.converter.ShopConverter
import com.byeduck.shoppinglist.common.converter.ShoppingListConverter
import com.byeduck.shoppinglist.login.LoginService
import com.byeduck.shoppinglist.model.ShopModel
import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.request.CreateShopRequest
import com.byeduck.shoppinglist.model.request.CreateShoppingElementRequest
import com.byeduck.shoppinglist.model.request.CreateShoppingListRequest
import com.byeduck.shoppinglist.model.view.Shop
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.byeduck.shoppinglist.model.view.ShoppingList
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ShoppingRepository {

    companion object {
        private val user = LoginService.getUser()
        private val db = FirebaseDatabase.getInstance()
        private val listsRefPath = "lists/${user.id}"
        private val shopsRefPath = "shops/${user.id}"

        fun getDbListsRef() = db.getReference(listsRefPath)

        fun getDbListElemRef(listId: String) = getDbListsRef().child(listId).child("elements")

        fun getDbShopsRef() = db.getReference(shopsRefPath)

        suspend fun insertShop(request: CreateShopRequest): String {
            val shopId = UUID.randomUUID().toString()
            db.getReference(shopsRefPath)
                .child(shopId)
                .setValue(
                    ShopModel(
                        shopId,
                        request.shopName,
                        request.description,
                        request.latitude,
                        request.longitude
                    )
                )
            return shopId
        }

        suspend fun insertList(request: CreateShoppingListRequest): String {
            val listId = UUID.randomUUID().toString()
            db.getReference(listsRefPath)
                .child(listId)
                .setValue(ShoppingListModel(listId, request.name))
            return listId
        }

        suspend fun insertListElement(request: CreateShoppingElementRequest): String {
            val listId = request.listId
            val elemId = UUID.randomUUID().toString()
            db.getReference(getElementsRefPath(listId, elemId))
                .setValue(ShoppingElementModel(elemId, request.text, request.price, request.count))
            return elemId
        }

        fun deleteShopById(shopId: String) =
            db.getReference(shopsRefPath)
                .child(shopId)
                .removeValue()

        fun deleteListElementById(listId: String, elemId: String) =
            db.getReference(getElementsRefPath(listId, elemId))
                .removeValue()

        fun deleteListById(listId: String) =
            db.getReference(listsRefPath)
                .child(listId)
                .removeValue()

        suspend fun updateShop(shop: Shop) {
            val shopId = shop.id
            val model = ShopConverter.modelFromShop(shop)
            db.getReference(shopsRefPath)
                .child(shopId)
                .setValue(shop)
        }

        suspend fun updateList(shoppingList: ShoppingList) {
            val listId = shoppingList.id
            val model = ShoppingListConverter.modelFromList(shoppingList)
            model.updatedAt = System.currentTimeMillis()
            db.getReference(listsRefPath)
                .child(listId)
                .setValue(model)
        }

        suspend fun updateElem(listId: String, shoppingElem: ShoppingElement) {
            db.getReference(getElementsRefPath(listId, shoppingElem.id))
                .setValue(shoppingElem)
        }

        private fun getElementsRefPath(listId: String, elemId: String) =
            "$listsRefPath/$listId/elements/$elemId"
    }
}