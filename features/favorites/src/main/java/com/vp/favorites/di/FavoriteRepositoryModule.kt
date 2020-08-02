package com.vp.favorites.di

import com.vp.favorites.repository.FavoriteRepository
import com.vp.favorites.repository.FavoriteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class FavoriteRepositoryModule {

    @Binds
    abstract fun provideFavoriteRepository(repository: FavoriteRepositoryImpl): FavoriteRepository
}