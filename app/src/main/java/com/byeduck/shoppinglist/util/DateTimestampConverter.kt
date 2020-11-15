package com.byeduck.shoppinglist.util

import androidx.room.TypeConverter
import java.util.*

class DateTimestampConverter {

    @TypeConverter
    fun fromTimestamp(timestamp: Long?): Date? = timestamp?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

}