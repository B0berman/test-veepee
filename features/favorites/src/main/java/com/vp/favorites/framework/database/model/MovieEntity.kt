package com.vp.favorites.framework.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vp.favorites.framework.database.Database

@Entity(
    tableName = Database.TABLE_MOVIES
)
data class MovieEntity(
    @PrimaryKey val id: String,
    val title: String = "",
    val poster: String = ""
)