package com.vp.favorites.di

import android.content.Context
import com.vp.favorites.service.FavoriteDataSource
import com.vp.favorites.service.FavoriteService
import com.vp.favorites.service.FavoriteServiceImpl
import com.vp.favorites.service.local.FavoriteLocalDataSource
import com.vp.favorites.service.local.MovieDao
import com.vp.favorites.service.local.MovieDatabase
import dagger.Module
import dagger.Provides

@Module
class FavoriteLocalRepositoryModule {

    @Provides
    fun providesFavoriteService(favoriteDataSource: FavoriteDataSource): FavoriteService {
        return FavoriteServiceImpl(favoriteDataSource)
    }

    @Provides
    fun provideFavoriteDataSource(context: Context): FavoriteDataSource{
        val movieDao: MovieDao = createDataBase(context).movieDao()
        return FavoriteLocalDataSource(movieDao)
    }

    private fun createDataBase(context: Context): MovieDatabase {
        return MovieDatabase.getDatabase(context)
    }
}