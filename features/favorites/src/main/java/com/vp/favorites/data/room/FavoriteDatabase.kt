package com.vp.favorites.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.favorites.core.model.room.Movie

@Database(entities = [Movie::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun getMovieDao() : MovieDao
}