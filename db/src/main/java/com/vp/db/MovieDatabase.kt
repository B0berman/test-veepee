package com.vp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
        entities = [
            MovieDB::class
        ],
        version = 1,
        exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}