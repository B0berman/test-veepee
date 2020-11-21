package com.vp.database.beans


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_movies")
data class Movie(@PrimaryKey val id: String,
                 val title: String,
                 val poster: String): Serializable