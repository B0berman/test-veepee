package com.vp.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.storage.dao.MovieItemDao
import com.vp.storage.model.MovieItemDTO

@Database(entities = [MovieItemDTO::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDoa(): MovieItemDao
}