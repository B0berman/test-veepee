package com.vp.favorites.di

import com.vp.favorites.view.FavoritesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoritesActivityModule {
    @ContributesAndroidInjector(modules = [FavoritesActivityModelModule::class])
    abstract fun bindFavoritesActivity(): FavoritesActivity
}