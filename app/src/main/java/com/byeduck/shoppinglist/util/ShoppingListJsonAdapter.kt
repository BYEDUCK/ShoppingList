package com.byeduck.shoppinglist.util

import android.util.Log
import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.*

class ShoppingListJsonAdapter : TypeAdapter<ShoppingListModel>() {
    override fun read(`in`: JsonReader?): ShoppingListModel {
        if (`in` == null) {
            throw IllegalArgumentException("Cannot read from null")
        }
        `in`.beginObject()
        var id: String? = null
        var name: String? = null
        val now = Date().time
        var createdAt: Long = now
        var updatedAt: Long = now
        var elements: List<ShoppingElementModel> = emptyList()
        while (`in`.hasNext()) {
            when (`in`.nextName()) {
                "id" -> id = `in`.nextString()
                "name" -> name = `in`.nextString()
                "createdAt" -> createdAt = `in`.nextLong()
                "updatedAt" -> updatedAt = `in`.nextLong()
                else -> Log.v("Converter", "Unknown field")
            }
        }
        `in`.endObject()
        return ShoppingListModel(
            id ?: throw IllegalArgumentException(),
            name ?: throw IllegalArgumentException(),
            createdAt, updatedAt, elements
        )
    }

    override fun write(out: JsonWriter?, value: ShoppingListModel?) {
        TODO("Not yet implemented")
    }
}