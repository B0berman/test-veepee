package com.vp.favorites.di

import com.vp.favorites.SharedPreferencesManager
import com.vp.favorites.SharedPreferencesManagerImpl
import dagger.Binds
import dagger.Module

@Module
abstract class SharedPreferencesModule {

    @Binds
    abstract fun provideSharedPreferences(sharedPreferences: SharedPreferencesManagerImpl): SharedPreferencesManager
}