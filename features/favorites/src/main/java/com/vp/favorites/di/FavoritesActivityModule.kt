package com.vp.favorites.di

import com.vp.api.model.FavoritesRepository
import com.vp.favorites.FavoriteActivity
import com.vp.favorites.repo.FavoritesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoritesActivityModule {

    @ContributesAndroidInjector
    abstract fun bindFavoriteActivity(): FavoriteActivity

    @Binds
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository
}