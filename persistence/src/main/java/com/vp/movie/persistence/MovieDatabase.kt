package com.vp.movie.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.movie.persistence.dao.FavouriteMovieDao
import com.vp.movie.persistence.entities.MovieEntity


object DatabaseMetaData {
    const val NAME = "movie.db"
    const val VERSION = 1
}

@Database(entities = [MovieEntity::class], version = DatabaseMetaData.VERSION)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun favouritesDao(): FavouriteMovieDao
}