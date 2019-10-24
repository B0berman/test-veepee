package com.vp.detail.di

import com.vp.detail.DetailActivity
import com.vp.storage.StorageModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailActivityModule {
    @ContributesAndroidInjector(modules = [DetailViewModelsModule::class, DetailNetworkModule::class, StorageModule::class])
    abstract fun bindDetailActivity(): DetailActivity
}