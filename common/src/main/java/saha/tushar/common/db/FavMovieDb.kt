package com.tushar.todosample.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import saha.tushar.common.db.DB_MOVIE_FAV_TABLE
import saha.tushar.common.db.FavMovieDao
import saha.tushar.common.db.FavMovie

@Database(entities = [FavMovie::class], version = 1)
abstract class FavMovieDb : RoomDatabase() {
    abstract fun favMovieDao(): FavMovieDao

    companion object {
        @Volatile
        private var INSTANCE: FavMovieDb? = null

        fun getDatabase(context: Context): FavMovieDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavMovieDb::class.java,
                        DB_MOVIE_FAV_TABLE
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}