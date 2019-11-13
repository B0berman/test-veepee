package com.vp.movies.persistence.entities

import androidx.room.Entity

@Entity(tableName = "favourites", primaryKeys = ["year"])
data class FavouriteEntity(
        val title:String,
        val year:String,
        val imbdID:String,
        val poster:String
)