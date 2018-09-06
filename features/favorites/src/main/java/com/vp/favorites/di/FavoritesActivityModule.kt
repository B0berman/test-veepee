package com.vp.favorites.di

import com.vp.database.DataBaseModule
import com.vp.favorites.FavoriteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoritesActivityModule {
    @ContributesAndroidInjector(modules = [DataBaseModule::class])
    abstract fun bindFavoritesActivity(): FavoriteActivity
}