package com.vp.favorites.di.module

import com.vp.favorites.FavoritesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FavoriteActivityModule {

    @ContributesAndroidInjector
    abstract fun bindsFavoriteActivity() : FavoritesActivity
}