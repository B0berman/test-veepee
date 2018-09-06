package com.vp.database

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.content.Context
import com.vp.database.model.FavoriteMovie

@Database(entities = [FavoriteMovie::class], version = 2)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao

    companion object {

        private var sInstance: FavoriteDatabase? = null

        @Synchronized
        fun getInstance(context: Context): FavoriteDatabase {
            if (sInstance == null) {
                sInstance = Room
                        .databaseBuilder(context.applicationContext, FavoriteDatabase::class.java, "favorites-database")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return sInstance!!
        }
    }

}