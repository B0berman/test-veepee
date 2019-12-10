package com.gmail.saneme87.roomdatabase.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteMovie(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "id")
        var id: String = "",

        @ColumnInfo(name = "poster")
        var poster: String = ""
)