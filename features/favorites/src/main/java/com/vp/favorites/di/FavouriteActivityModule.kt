package com.vp.list.di

import com.vp.favorites.FavoriteActivity
import com.vp.favorites.FavouriteListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavouriteActivityModule {

    @ContributesAndroidInjector(modules = [FavouriteListFragmentModule::class,FavouriteListViewModelsModule::class])
    abstract fun bindFavouriteActivity(): FavoriteActivity
}