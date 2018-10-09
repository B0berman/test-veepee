package com.vp.favorites.di

import com.vp.favorites.FavoriteListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteListActivityModule {

    @ContributesAndroidInjector(modules = [FavoriteViewModelModule::class])
    abstract fun bindMovieListActivity(): FavoriteListActivity

}