package com.vp.favorites.di

import com.vp.favorites.FavoriteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Uxio Lorenzo on 2019-09-10.
 */
@Module
abstract class FavoritesActivityModule {

    @ContributesAndroidInjector(modules = [FavoriteViewModelsModule::class])
    abstract fun bindFavoriteActivity(): FavoriteActivity
}