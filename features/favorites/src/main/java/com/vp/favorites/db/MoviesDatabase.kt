package com.vp.favorites.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vp.api.model.ListItem

@Database(entities = [ListItem::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun favouriteDao(): FavoritesDao

    companion object {
        @Volatile
        private var instance: MoviesDatabase? = null

        fun get(context: Context): MoviesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MoviesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                "Movies.db"
            ).build()

        }
    }
}