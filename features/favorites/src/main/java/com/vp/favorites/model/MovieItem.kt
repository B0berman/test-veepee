package com.vp.favorites.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_table")
data class MovieItem @JvmOverloads constructor(
        @PrimaryKey(autoGenerate = true)
                     @ColumnInfo(name = "id") var id:Int? = null,

        @ColumnInfo(name = "title")
        var title:String,

        @ColumnInfo(name = "year")
        var year:String,

        @ColumnInfo(name = "imdbID")
        var imdbID:String,

        @ColumnInfo(name = "director")
        var director:String,

        @ColumnInfo(name = "poster")
        var poster:String
)