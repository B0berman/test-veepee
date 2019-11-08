package com.vp.favorites.di

import android.app.Activity
import com.vp.favorites.FavoriteListActivity
import com.vp.navigation.NavigationModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteListModule {

    @ContributesAndroidInjector(modules = [FavoriteListActivityModule::class])
    internal abstract fun bindFavoriteListActivity(): FavoriteListActivity

    @Module(includes = [NavigationModule::class, FavoriteListFragmentModule::class, FavoriteListViewModelsModule::class])
    abstract class FavoriteListActivityModule {

        @Binds
        internal abstract fun activity(activity: FavoriteListActivity): Activity
    }
}