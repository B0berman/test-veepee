package com.vp.favorites.di

import com.vp.favorites.FavoriteActivity
import dagger.android.ContributesAndroidInjector

abstract class FavouriteActivityModule {
    @ContributesAndroidInjector(modules = [
        FavouriteViewModelModule::class,
        FavouritesPersistenceModule::class])
    abstract fun bindDetailActivity(): FavoriteActivity
}