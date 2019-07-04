package com.vp.favorites.persistence.room

import androidx.room.TypeConverter
import java.util.*

internal class DateTypeConverter {

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(date: Long) = Date(date)
}