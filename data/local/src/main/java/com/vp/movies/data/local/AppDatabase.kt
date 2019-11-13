package com.vp.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.movies.data.local.dao.MovieDao
import com.vp.movies.data.model.MovieEntity

@Database(
        entities = [
            MovieEntity::class
        ],
        version = 1,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}