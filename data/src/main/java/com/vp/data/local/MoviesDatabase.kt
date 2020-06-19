package com.vp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vp.data.model.MovieFavorite

@Database(
    entities = [MovieFavorite::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun movieDao(): FavoritesDao

    companion object {

        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        fun getInstance(context: Context): MoviesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java, "movies.db"
            ).build()
    }
}
