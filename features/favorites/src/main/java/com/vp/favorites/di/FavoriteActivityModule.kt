package com.vp.favorites.di

import com.vp.favorites.FavoriteActivity
import com.vp.persistence.di.PersistenceModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteActivityModule {

    @ContributesAndroidInjector(modules = [ListFragmentModule::class, ListViewModelsModule::class, PersistenceModule::class])
    abstract fun bindMovieListActivity(): FavoriteActivity
}