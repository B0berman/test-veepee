package com.vp.list.di

import com.vp.list.MovieListActivity
import com.vp.persistence.di.PersistenceModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MovieListActivityModule {

    @ContributesAndroidInjector(modules = [ListFragmentModule::class, ListNetworkModule::class, ListViewModelsModule::class, PersistenceModule::class])
    abstract fun bindMovieListActivity(): MovieListActivity
}