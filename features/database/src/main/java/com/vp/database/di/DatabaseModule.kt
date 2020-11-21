package com.vp.database.di

import android.app.Application
import androidx.room.Room
import com.vp.database.db.MovieDatabase
import com.vp.database.db.dao.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    private var movieDatabase: MovieDatabase? = null

    constructor(mApplication: Application?) {
        movieDatabase = Room.databaseBuilder(mApplication!!.applicationContext, MovieDatabase::class.java, "movies.db").build()
    }

    @Singleton
    @Provides
    fun providesRoomDatabase(): MovieDatabase? {
        return movieDatabase
    }

    @Singleton
    @Provides
    fun providesMovieDao(database: MovieDatabase): MovieDao? {
        return database.movieDao()
    }
}