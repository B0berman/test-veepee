package com.vp.storage

import android.app.Application
import androidx.room.Room
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataBaseManager @Inject constructor() {
    private var db : MovieDatabase? = null
    fun initDataBase(application: Application) {
      db =  Room.databaseBuilder(application, MovieDatabase::class.java, "movie-db").build()
    }

    fun getMovieDatabase() = db?: throw IllegalStateException("Please initDb by initDataBase(app) method ")
}