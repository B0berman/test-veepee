package com.vp.favorites.di

import com.vp.favorites.data.FavoritesLocalDataSourceImpl
import com.vp.favorites.datasource.FavoritesLocalDataSource
import com.vp.storage.MoviesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FavoritesLocalDataSourceModule {

    @Singleton
    @Provides
    fun providesFavoritesLocalDataSource(movieDataBase: MoviesDatabase): FavoritesLocalDataSource =
            FavoritesLocalDataSourceImpl(movieDataBase)
}
