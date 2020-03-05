package com.vp.list.di

import com.vp.list.MovieListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [
    ListFragmentModule::class
])
abstract class MovieListActivityModule {

    @ContributesAndroidInjector
    abstract fun bindMovieListActivity(): MovieListActivity
}