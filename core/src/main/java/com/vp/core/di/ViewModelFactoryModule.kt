package com.vp.core.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindDaggerViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}