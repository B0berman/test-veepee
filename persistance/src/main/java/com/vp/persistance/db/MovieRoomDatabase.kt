/*
 * Created by Alexis Rodriguez Paret on 3/13/20 8:59 PM
 * Copyright (c) 2020. All rights reserved. Heliocor (alexis.rodriquez@heliocor.com)
 * Last modified 3/13/20 8:59 PM
 *
 */

package com.vp.persistance.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vp.persistance.dao.MovieDao
import com.vp.persistance.model.MovieModel
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Alexis Rodríguez Paret on 2020-03-13.
 */
@Database(entities = arrayOf(MovieModel::class), version = 1, exportSchema = false)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope): MovieRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieRoomDatabase::class.java,
                        "movie_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}