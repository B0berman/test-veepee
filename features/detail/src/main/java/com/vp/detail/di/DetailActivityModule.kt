package com.vp.detail.di

import com.vp.detail.DetailActivity
import com.vp.movie.domain.DomainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailActivityModule {
    @ContributesAndroidInjector(modules = [
        DetailViewModelsModule::class,
        DetailNetworkModule::class,
        DetailPersistenceModule::class])
    abstract fun bindDetailActivity(): DetailActivity
}