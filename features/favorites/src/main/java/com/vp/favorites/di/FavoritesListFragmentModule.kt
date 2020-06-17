package com.vp.favorites.di

import com.vp.favorites.FavoritesListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoritesListFragmentModule {

    @ContributesAndroidInjector
    abstract fun bindFavoritesListFragment(): FavoritesListFragment

}
