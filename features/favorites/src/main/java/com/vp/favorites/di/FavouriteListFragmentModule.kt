package com.vp.list.di

import com.vp.favorites.FavouriteListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavouriteListFragmentModule {

    @ContributesAndroidInjector
    abstract fun bindFavouriteListFragment(): FavouriteListFragment
}