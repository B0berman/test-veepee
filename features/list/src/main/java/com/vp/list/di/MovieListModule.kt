package com.vp.list.di

import android.app.Activity
import com.vp.list.MovieListActivity
import com.vp.navigation.NavigationModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MovieListModule {

    @ContributesAndroidInjector(modules = [MovieListActivityModule::class])
    internal abstract fun bindMovieListActivity(): MovieListActivity

    @Module(includes = [NavigationModule::class, ListFragmentModule::class, ListViewModelsModule::class])
    abstract class MovieListActivityModule {

        @Binds
        internal abstract fun activity(activity: MovieListActivity): Activity
    }
}