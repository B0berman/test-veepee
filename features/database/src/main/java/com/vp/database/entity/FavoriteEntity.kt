package com.vp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteEntity(
        @PrimaryKey
        @ColumnInfo(name = "imdb_id")
        var imdbID: String,
        @ColumnInfo(name = "title")
        var title: String?,
        @ColumnInfo(name = "poster")
        var poster: String?
)