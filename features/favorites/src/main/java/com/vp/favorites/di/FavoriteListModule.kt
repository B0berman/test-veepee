package com.vp.favorites.di

import android.app.Activity
import com.vp.favorites.FavoriteListActivity
import com.vp.navigation.NavigationModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteListActivityModule {

    @Binds
    abstract fun activity(activity: FavoriteListActivity): Activity

    @ContributesAndroidInjector(modules = [NavigationModule::class, FavoriteListFragmentModule::class, FavoriteListViewModelsModule::class])
    abstract fun bindMovieListActivity(): FavoriteListActivity
}