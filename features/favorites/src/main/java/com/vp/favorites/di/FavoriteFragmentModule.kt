package com.vp.favorites.di

import com.vp.favorites.FavoriteListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FavoriteFragmentModule {

    @ContributesAndroidInjector
    fun bindListFragment(): FavoriteListFragment
}
