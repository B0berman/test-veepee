package com.vp.detail.di

import com.vp.detail.DetailActivity
import com.vp.favorites.di.FavoriteMovieDatabaseModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailActivityModule {
    @ContributesAndroidInjector(modules = [DetailViewModelsModule::class, DetailNetworkModule::class, FavoriteMovieDatabaseModule::class])
    abstract fun bindDetailActivity(): DetailActivity
}