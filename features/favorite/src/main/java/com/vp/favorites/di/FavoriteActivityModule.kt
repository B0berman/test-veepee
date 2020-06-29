package com.vp.favorites.di

import com.vp.detail.di.DetailNetworkModule
import com.vp.favorites.FavoriteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteActivityModule {
    @ContributesAndroidInjector(modules = [FavoriteViewModelsModule::class,  DetailNetworkModule::class])
    abstract fun bindActivity(): FavoriteActivity
}