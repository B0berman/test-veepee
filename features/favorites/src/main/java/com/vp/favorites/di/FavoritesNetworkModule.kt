package com.vp.favorites.di

import com.vp.favorites.service.FavoritesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class FavoritesNetworkModule {
    @Provides
    fun providesFavoriteService(retrofit: Retrofit): FavoritesService {
        return retrofit.create(FavoritesService::class.java)
    }
}