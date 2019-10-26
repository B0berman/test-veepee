package com.vp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieDB(
        @PrimaryKey
        val id: String,
        val title: String,
        val year: String,
        val runtime: String,
        val director: String,
        val plot: String,
        val poster: String
)