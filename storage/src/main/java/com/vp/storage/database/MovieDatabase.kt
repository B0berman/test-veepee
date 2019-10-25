package com.vp.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.storage.model.MovieDetailDB

@Database(entities = [MovieDetailDB::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}