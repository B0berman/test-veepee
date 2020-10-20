package com.vp.favorites.di

import com.vp.favorite.di.FavoriteMovieModule
import com.vp.favorites.FavoriteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteActivityModule {

    @ContributesAndroidInjector(
        modules = [
            FavoriteFragmentModule::class,
            FavoriteViewModelsModule::class,
            FavoriteMovieModule::class
        ]
    )
    abstract fun bindFavoriteActivity(): FavoriteActivity
}
