package com.gmail.saneme87.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.saneme87.roomdatabase.model.FavoriteMovie

@Database(entities = [FavoriteMovie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}