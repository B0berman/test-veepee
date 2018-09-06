package com.vp.database.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteMovie(@PrimaryKey val id: String, @ColumnInfo(name = "title") val title: String)