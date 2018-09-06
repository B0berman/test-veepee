package com.vp.detail.di

import com.vp.database.DataBaseModule
import com.vp.detail.DetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailActivityModule {
    @ContributesAndroidInjector(modules = [DetailViewModelsModule::class, DetailNetworkModule::class, DataBaseModule::class])
    abstract fun bindDetailActivity(): DetailActivity
}