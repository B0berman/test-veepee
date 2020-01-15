package com.vp.detail.di

import com.vp.detail.DetailActivity
import com.vp.persistence.di.PersistenceModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailActivityModule {
    @ContributesAndroidInjector(modules = [DetailViewModelsModule::class, DetailNetworkModule::class, PersistenceModule::class])
    abstract fun bindDetailActivity(): DetailActivity
}