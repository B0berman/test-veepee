package com.vp.favorites.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vp.detail.model.FavoriteMovie

@Database(entities = arrayOf(FavoriteMovie::class), version = 1)
abstract class FavoritesMoviesDb : RoomDatabase() {
    abstract fun favoriteMoviesDao(): FavoriteMoviesDao

    companion object {
        const val DB_NAME = "favoritesMovies.db"
        private var INSTANCE: FavoritesMoviesDb? = null
        fun getInstance(context: Context): FavoritesMoviesDb {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context,
                        FavoritesMoviesDb::class.java,
                        DB_NAME).allowMainThreadQueries()
                        .build()
            }
            return INSTANCE as FavoritesMoviesDb
        }
    }
}