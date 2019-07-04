package com.vp.favorites.persistence.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vp.favorites.persistence.room.model.FavoriteMovieEntity

@Database(
        entities = [FavoriteMovieEntity::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(DateTypeConverter::class)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun getDao(): FavoritesDao

    object TableNames {
        const val FAVORITE_MOVIES = "favorite_movies"
    }

    object ColumnNames {
        const val ID = "movie_id"
        const val DATE_ADDED = "date_added"
    }

    companion object {
        fun create(context: Context): FavoritesDatabase = Room
                .databaseBuilder(context, FavoritesDatabase::class.java, "favorites.db")
                .build()
    }
}