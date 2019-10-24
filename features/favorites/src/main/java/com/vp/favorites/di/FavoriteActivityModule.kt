package com.vp.favorites.di

import com.vp.favorites.FavoriteActivity
import com.vp.storage.StorageModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteActivityModule {
    @ContributesAndroidInjector(modules = [FavoriteViewModelsModule::class, StorageModule::class])
    abstract fun bindFavoriteActivity(): FavoriteActivity
}