package com.vp.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieDetailDB::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}