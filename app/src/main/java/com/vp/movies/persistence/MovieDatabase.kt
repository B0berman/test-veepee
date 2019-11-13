package com.vp.movies.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.movies.persistence.dao.FavouriteDao
import com.vp.movies.persistence.entities.FavouriteEntity

object DatabaseMetaData {
    const val NAME = "movie.db"
    const val VERSION = 1
}

@Database(entities = [FavouriteEntity::class], version = DatabaseMetaData.VERSION)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun favouritesDao(): FavouriteDao
}