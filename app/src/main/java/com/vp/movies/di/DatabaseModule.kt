package com.vp.movies.di

import android.app.Application
import com.vp.favorites.db.MoviesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(appContext: Application) = MoviesDatabase.get(appContext)

    @Provides
    fun provideFavoritesDao(database: MoviesDatabase) = database.favouriteDao()
}