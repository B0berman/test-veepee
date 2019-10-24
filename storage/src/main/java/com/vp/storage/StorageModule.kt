package com.vp.storage

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java, "database-test").build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(database: MovieDatabase): MoviesDao = database.moviesDao()
}