package com.vp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "favorites")
data class MovieFavorite(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val title: String,
    val year: String,
    val runtime: String,
    val director: String,
    val plot: String,
    val poster: String,
    var timePersisted: Date = Date()
)
