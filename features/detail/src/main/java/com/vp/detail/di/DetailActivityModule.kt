package com.vp.detail.di

import com.vp.detail.DetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import saha.tushar.common.di.DbModule

@Module
abstract class DetailActivityModule {
    @ContributesAndroidInjector(modules = [DetailViewModelsModule::class, DetailNetworkModule::class, DbModule::class])
    abstract fun bindDetailActivity(): DetailActivity
}