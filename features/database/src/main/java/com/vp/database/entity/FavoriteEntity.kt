package com.vp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteEntity(
        @PrimaryKey
        @ColumnInfo(name = "imdb_id")
        var imdbID: Int,
        @ColumnInfo(name = "title")
        var title: String?,
        @ColumnInfo(name = "year")
        var year: String?,
        @ColumnInfo(name = "poster")
        var poster: String?,
        @ColumnInfo(name = "runtime")
        var runtime: String?,
        @ColumnInfo(name = "director")
        var director: String?,
        @ColumnInfo(name = "plot")
        var plot: String?
)