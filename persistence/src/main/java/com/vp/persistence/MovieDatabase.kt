package com.vp.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MovieEntity::class), version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun moviesDao() : MovieDao
}