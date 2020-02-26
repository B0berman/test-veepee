package com.vp.favorites.di

import android.app.Application
import android.content.Context
import com.vp.favorites.FavoriteActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class FavoriteActivityModule {
    @ContributesAndroidInjector(modules = [FavoriteFragmentModule::class, FavoriteLocalRepositoryModule::class, FavoriteViewModelsModule::class])
    abstract fun bindFavoriteActivity(): FavoriteActivity


    @Binds
    @Singleton
    abstract fun bindApplicationContext(application: Application): Context
}