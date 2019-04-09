package com.vp.databases.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteMovie(@PrimaryKey(autoGenerate = true) var uid: Long?,
                         @ColumnInfo(name = "title") var title: String?,
                         @ColumnInfo(name = "year") var year: String?,
                         @ColumnInfo(name = "imdbID") var imdbID: String?,
                         @ColumnInfo(name = "poster") var poster: String?) {
    constructor() : this(null, "", "", "", "")
}