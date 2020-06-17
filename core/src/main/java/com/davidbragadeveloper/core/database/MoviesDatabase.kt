package com.davidbragadeveloper.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.davidbragadeveloper.core.database.dao.MovieDao
import com.davidbragadeveloper.core.entity.MovieRoomEntity

@Database(entities = [MovieRoomEntity::class], version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    companion object{
        fun build(context: Context): MoviesDatabase =
                Room.databaseBuilder(
                        context,
                        MoviesDatabase::class.java,
                        "Movies-db"
                ).build()
    }

    abstract fun movieDao(): MovieDao
}