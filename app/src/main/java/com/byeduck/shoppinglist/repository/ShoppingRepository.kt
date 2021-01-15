package com.byeduck.shoppinglist.repository

import com.byeduck.shoppinglist.common.converter.ShopConverter
import com.byeduck.shoppinglist.login.LoginService
import com.byeduck.shoppinglist.model.PromotionModel
import com.byeduck.shoppinglist.model.ShopModel
import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.request.*
import com.byeduck.shoppinglist.model.view.Promotion
import com.byeduck.shoppinglist.model.view.ShoppingElement
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.math.abs

class ShoppingRepository {

    companion object {
        private val user = LoginService.getUser()
        private val db = FirebaseDatabase.getInstance()
        private val listsRefPath = "lists/${user.id}"
        private val shopsRefPath = "shops/${user.id}"

        fun getDbListsRef() = db.getReference(listsRefPath)

        fun getDbListElemRef(listId: String) = getDbListsRef()
            .child(listId)
            .child("elements")

        fun getDbShopsRef() = db.getReference(shopsRefPath)

        fun getDbPromosRef(shopId: String) = getDbShopsRef()
            .child(shopId)
            .child("promos")

        fun insertPromo(request: CreatePromoRequest): Task<Void> {
            val promoId = generatePromoId(request.shopId, request.date)
            return getDbPromosRef(request.shopId)
                .child(promoId)
                .setValue(
                    PromotionModel(
                        promoId,
                        request.shopId,
                        request.shopName,
                        request.promoName,
                        request.promoShortDesc,
                        request.promoFullDesc,
                        request.date
                    )
                )
        }

        fun insertShop(request: CreateShopRequest): Task<String> {
            val shopId = UUID.randomUUID().toString()
            val taskCompletionSource = TaskCompletionSource<String>()
            return db.getReference(shopsRefPath)
                .child(shopId)
                .get()
                .onSuccessTask {
                    if (!it.exists()) {
                        db.getReference(shopsRefPath)
                            .child(shopId)
                            .setValue(
                                ShopModel(
                                    shopId,
                                    request.shopName,
                                    request.description,
                                    request.latitude,
                                    request.longitude,
                                    request.radius
                                )
                            ).onSuccessTask {
                                taskCompletionSource.setResult(shopId)
                                taskCompletionSource.task
                            }
                    } else {
                        taskCompletionSource.setException(IllegalArgumentException("Shop already exists"))
                        taskCompletionSource.task
                    }
                }
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

        fun deletePromoById(promoId: String, shopId: String) =
            db.getReference(getPromoRefPath(shopId))
                .child(promoId)
                .removeValue()

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

        fun updatePromo(promo: Promotion): Task<Void> {
            val model = ShopConverter.modelFromPromotion(promo)
            return db.getReference(getPromoRefPath(promo.shopId))
                .child(promo.id)
                .setValue(model)
        }

        fun updateShop(request: UpdateShopRequest): Task<Void> {
            return db.getReference(shopsRefPath)
                .child(request.shopId)
                .updateChildren(
                    mapOf(
                        "name" to request.shopName,
                        "description" to request.shopDescription,
                        "radius" to request.radius
                    )
                )
        }

        suspend fun updateList(request: UpdateShoppingListRequest) {
            val updatedAt = System.currentTimeMillis()
            db.getReference(listsRefPath)
                .child(request.listId)
                .updateChildren(
                    mapOf(
                        "name" to request.listName,
                        "updatedAt" to updatedAt
                    )
                )
        }

        suspend fun updateElem(listId: String, shoppingElem: ShoppingElement) {
            db.getReference(getElementsRefPath(listId, shoppingElem.id))
                .setValue(shoppingElem)
        }

        private fun getElementsRefPath(listId: String, elemId: String) =
            "$listsRefPath/$listId/elements/$elemId"

        private fun getPromoRefPath(shopId: String) =
            "$shopsRefPath/$shopId/promos"

        private fun generatePromoId(shopId: String, date: String): String {
            return abs(Objects.hash(shopId, date)).toString(16)
        }
    }
}