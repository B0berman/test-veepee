package com.vp.favorites.persistence.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vp.favorites.persistence.room.FavoritesDatabase.ColumnNames.DATE_ADDED
import com.vp.favorites.persistence.room.FavoritesDatabase.ColumnNames.ID
import com.vp.favorites.persistence.room.FavoritesDatabase.TableNames.FAVORITE_MOVIES
import java.util.*

@Entity(tableName = FAVORITE_MOVIES)
data class FavoriteMovieEntity(
        @PrimaryKey @ColumnInfo(name = ID) val id: String,
        @ColumnInfo(name = DATE_ADDED) val dateAdded: Date,
        val title: String,
        val year: String,
        val director: String,
        val poster: String?
)