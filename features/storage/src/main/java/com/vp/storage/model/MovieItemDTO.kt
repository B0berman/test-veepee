package com.vp.storage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieItemDTO(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "imdbID") val imdbID: String,
        @ColumnInfo(name = "Title") val title: String?,
        @ColumnInfo(name = "Year") val year: String?,
        @ColumnInfo(name = "Poster") val poster: String?
)