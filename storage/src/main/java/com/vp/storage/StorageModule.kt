package com.vp.storage

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides

@Module
class StorageModule {

    @Provides
    fun provideDatabase(context: Application): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java, "database-test").build()
    }

    @Provides
    fun provideMovieDao(database: MovieDatabase): MoviesDao = database.moviesDao()
}