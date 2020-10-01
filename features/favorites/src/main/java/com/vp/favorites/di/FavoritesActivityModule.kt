package com.vp.favorites.di

import com.vp.favorites.FavoritesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoritesActivityModule {
    @ContributesAndroidInjector(modules = [FavoritesViewModelsModule::class, FavoritesNetworkModule::class])
    abstract fun bindFavoritesActivity(): FavoritesActivity
}