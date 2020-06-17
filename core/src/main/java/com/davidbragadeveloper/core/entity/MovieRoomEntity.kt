package com.davidbragadeveloper.core.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movie")
data class MovieRoomEntity (
        @PrimaryKey val id: String,
        val title: String,
        val year: String,
        val runtime: String? = null,
        val director: String? = null,
        val plot: String? = null,
        val poster: String,
        val favorite: Boolean? = false
)