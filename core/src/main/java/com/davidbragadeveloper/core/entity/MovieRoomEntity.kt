package com.davidbragadeveloper.core.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movie")
data class MovieRoomEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name="title") val title: String,
        @ColumnInfo(name="year") val year: String,
        @ColumnInfo(name="runtime") val runtime: String,
        @ColumnInfo(name="director") val director: String,
        @ColumnInfo(name="plot") val plot: String,
        @ColumnInfo(name="poster") val poster: String,
        @ColumnInfo(name="favorite") val favorite: Boolean = false
)