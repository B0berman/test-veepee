package com.vp.favorites.data.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vp.favorites.data.models.db.FavoriteDB

@Database(
    entities = [
      (FavoriteDB::class)
    ],
    version = 2
)
abstract class AppDB : RoomDatabase() {
  abstract fun favoriteDao(): FavoriteDao

  companion object {

    @Volatile
    private var INSTANCE: AppDB? = null

    private const val dbName = "veepee-project.db"

    fun init(context: Context) {
      if (INSTANCE == null) {
        synchronized(AppDB::class) {
          INSTANCE =
            Room.databaseBuilder(context.applicationContext, AppDB::class.java,
                dbName
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
      }
    }

    fun getInstance(): AppDB {
      Log.i("DB", "AppDB getInstance")
      if (INSTANCE == null) {
        Log.wtf("Room", "Room Database not initialized")
        throw(IllegalAccessError())
      } else
        return INSTANCE!!

    }

    private fun destroyInstance() {
      INSTANCE = null
    }
  }
}