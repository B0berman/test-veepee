package com.vp.favorites.data.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

internal const val tableName = "FAVORITE"
internal const val ID = "_id"
internal const val FAVORITE_IMAGE = "IMAGE"
internal const val FAVORITE_TITLE = "TITLE"

@Entity(tableName = tableName)
data class FavoriteDB(
  @PrimaryKey(autoGenerate = false) @ColumnInfo(name = ID) var id: String,
  @ColumnInfo(name = FAVORITE_IMAGE) var favoriteImage: String,
  @ColumnInfo(name = FAVORITE_TITLE) var favoriteTitle: String
)