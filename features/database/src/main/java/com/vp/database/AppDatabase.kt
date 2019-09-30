package com.vp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.database.dao.FavoriteDao
import com.vp.database.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}