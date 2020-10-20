package com.vp.detail.di

import com.vp.detail.DetailActivity
import com.vp.favorite.di.FavoriteMovieModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailActivityModule {
    @ContributesAndroidInjector(
        modules = [
            DetailViewModelsModule::class,
            DetailNetworkModule::class,
            FavoriteMovieModule::class
        ]
    )
    abstract fun bindDetailActivity(): DetailActivity
}
