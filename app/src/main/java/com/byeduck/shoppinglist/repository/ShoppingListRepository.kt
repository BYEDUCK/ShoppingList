package com.byeduck.shoppinglist.repository

import com.byeduck.shoppinglist.login.LoginService
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.model.request.CreateShoppingListRequest
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ShoppingListRepository {

    companion object {
        private val user = LoginService.getUser()
        private val db = FirebaseDatabase.getInstance()
        private val listsRefPath = "lists/${user.id}"

        fun getDbRef() = db.getReference(listsRefPath)

        suspend fun insert(request: CreateShoppingListRequest): String {
            val listId = UUID.randomUUID().toString()
            db.getReference(listsRefPath)
                .child(listId)
                .setValue(ShoppingListModel(listId, request.name))
            return listId
        }

        fun deleteById(listId: String) = db.getReference(listsRefPath).child(listId).removeValue()
    }

//    fun getById(id: Long): LiveData<ShoppingListWithElements> {
//        val model = shoppingListDao.getByIdWithElements(id)
//        return Transformations.map(model) { list ->
//            list?.let {
//                ShoppingListWithElements.fromModel(it)
//            }
//        }
//    }

//    suspend fun edit(shoppingList: ShoppingList) = // TODO: implement
}