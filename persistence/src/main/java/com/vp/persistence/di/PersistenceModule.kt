package com.vp.persistence.di

import android.app.Application
import androidx.room.Room
import com.vp.persistence.MovieDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Singleton
    @Provides
    fun providesDatabase(application:Application) : MovieDatabase =
        Room.databaseBuilder(application, MovieDatabase::class.java, "movie_database").build()

    @Singleton
    @Provides
    fun providesMoviesDao(database:MovieDatabase) = database.moviesDao()

}