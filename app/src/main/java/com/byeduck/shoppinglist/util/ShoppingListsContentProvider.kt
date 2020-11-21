package com.byeduck.shoppinglist.util

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.byeduck.shoppinglist.db.ShoppingDB
import com.byeduck.shoppinglist.db.repository.ShoppingListRepository

class ShoppingListsContentProvider : ContentProvider() {

    private lateinit var repository: ShoppingListRepository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun onCreate(): Boolean {
        val shoppingListDao = ShoppingDB.getDatabase(
            context ?: return false
        ).shoppingListDao()
        repository = ShoppingListRepository(shoppingListDao)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return repository.getAll()
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}