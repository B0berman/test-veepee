package com.vp.list.di.module

import com.vp.list.MovieListActivity
import com.vp.list.di.ListFragmentModule
import com.vp.list.di.ListViewModelsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TestMovieListActivityModule {

    @ContributesAndroidInjector(modules = [ListFragmentModule::class, ListViewModelsModule::class])
    abstract fun bindMovieListActivity(): MovieListActivity

}