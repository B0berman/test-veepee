package com.vp.favorites.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.favorites.framework.database.dao.MovieDao
import com.vp.favorites.framework.database.model.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        const val DB_NAME = "database"

        const val TABLE_MOVIES = "table_movies"
    }
}