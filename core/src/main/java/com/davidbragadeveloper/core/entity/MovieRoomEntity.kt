package com.davidbragadeveloper.core.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Movie")
data class MovieRoomEntity (
        @PrimaryKey @ColumnInfo(name= "id") val id: String,
        @ColumnInfo(name= "title") val title: String,
        @ColumnInfo(name= "year") val year: String,
        @ColumnInfo(name= "runtime") val runtime: String? = null,
        @ColumnInfo(name= "director") val director: String? = null,
        @ColumnInfo(name= "plot") val plot: String? = null,
        @ColumnInfo(name= "poster") val poster: String,
        @ColumnInfo(name= "favorite") val favorite: Boolean? = false
)