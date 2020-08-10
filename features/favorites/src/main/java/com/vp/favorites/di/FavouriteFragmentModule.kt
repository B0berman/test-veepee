package com.vp.favorites.di

import com.vp.favorites.FavouriteListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavouriteFragmentModule {


    @ContributesAndroidInjector
    abstract fun bindFavouriteListFragment(): FavouriteListFragment
}