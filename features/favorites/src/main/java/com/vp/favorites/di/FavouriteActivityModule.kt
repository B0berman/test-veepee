package com.vp.favorites.di

import com.vp.favorites.FavoriteActivity
import com.vp.list.di.ListNetworkModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavouriteActivityModule {

    @ContributesAndroidInjector(modules = [FavouriteViewModelModule::class, FavouriteFragmentModule::class, ListNetworkModule::class])
    abstract fun bindDetailActivity(): FavoriteActivity
}
