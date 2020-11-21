package com.vp.database

import android.content.Context
import androidx.room.Room
import com.vp.database.db.MovieDatabase

object DatabaseManager {

    fun getDB(context: Context) = Room.databaseBuilder(context, MovieDatabase::class.java, "movies.db").build()

}