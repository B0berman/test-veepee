package com.vp.list.di

import android.app.Activity
import com.vp.list.MovieListActivity
import com.vp.navigation.NavigationModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MovieListActivityModule {

    @Binds
    abstract fun activity(activity: MovieListActivity): Activity

    @ContributesAndroidInjector(modules = [
        NavigationModule::class,
        ListFragmentModule::class,
        ListNetworkModule::class,
        ListViewModelsModule::class,
        ListDBModule::class
    ])
    abstract fun bindMovieListActivity(): MovieListActivity
}