package com.vp.favorites.di

import com.vp.favorites.presentation.ui.FavoriteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteActivityModule {
  @ContributesAndroidInjector(modules = [FavoriteViewModelsModule::class])
  abstract fun bindFavoriteActivity(): FavoriteActivity
}