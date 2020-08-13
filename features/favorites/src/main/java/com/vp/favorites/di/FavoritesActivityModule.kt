package com.vp.favorites.di

import com.vp.favorites.FavoriteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoritesActivityModule {
    @ContributesAndroidInjector(modules = [FavoriteViewModelsModule::class])
    abstract fun bindFavoritesActivity(): FavoriteActivity
}
