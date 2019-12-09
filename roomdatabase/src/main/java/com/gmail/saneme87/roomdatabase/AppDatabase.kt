package com.gmail.saneme87.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.saneme87.roomdatabase.model.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}