package com.vp.favorites.service.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vp.favorites.model.MovieItem

@Database(entities = [MovieItem::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: MovieDatabase

        fun getDatabase(context: Context): MovieDatabase {
            if(!::INSTANCE.isInitialized)
            {
                INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java, "movie_database"
                ).build()
            }
            return INSTANCE
        }
    }
}