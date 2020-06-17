package com.vp.favorites.di

import com.vp.favorites.FavoriteListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoritesListActivityModule {
    @ContributesAndroidInjector(modules = [
        FavoritesListFragmentModule::class,
        FavoritesViewModelModule::class
    ])
    abstract fun bindFavoritesListActivity(): FavoriteListActivity
}