package com.vp.favorites.di

import com.vp.favorites.FavoriteActivity
import com.vp.moviedatabase.di.MovieDatabaseModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteActivityModule {
    @ContributesAndroidInjector(modules = [FavoriteMoviesFragmentModule::class,
        FavoriteMoviesViewModelModule::class, MovieDatabaseModule::class])
    abstract fun bindFavoriteActivity(): FavoriteActivity
}