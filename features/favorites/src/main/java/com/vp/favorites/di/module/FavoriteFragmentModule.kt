package com.vp.favorites.di.module

import com.vp.favorites.fragment.FavoriteFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteFragmentModule {



    @ContributesAndroidInjector(modules = [FavoriteViewModelModule::class])
    abstract fun bindFavoriteFragment(): FavoriteFragment

}