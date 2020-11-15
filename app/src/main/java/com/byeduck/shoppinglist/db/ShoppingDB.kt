package com.byeduck.shoppinglist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.byeduck.shoppinglist.db.dao.ShoppingElementDao
import com.byeduck.shoppinglist.db.dao.ShoppingListDao
import com.byeduck.shoppinglist.model.ShoppingElementModel
import com.byeduck.shoppinglist.model.ShoppingListModel
import com.byeduck.shoppinglist.util.DateTimestampConverter

@Database(entities = [ShoppingListModel::class, ShoppingElementModel::class], version = 1)
@TypeConverters(DateTimestampConverter::class)
abstract class ShoppingDB : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun shoppingElementDao(): ShoppingElementDao

    companion object {
        private var dbInstance: ShoppingDB? = null

        fun getDatabase(context: Context): ShoppingDB {
            val tmpInstance = dbInstance
            if (tmpInstance != null) {
                return tmpInstance
            }
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ShoppingDB::class.java,
                "shopping_DB"
            ).build()
            dbInstance = instance
            return instance
        }
    }

}