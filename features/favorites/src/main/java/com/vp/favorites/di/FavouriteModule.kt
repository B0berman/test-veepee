package com.vp.favorites.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vp.daggeraddons.DaggerViewModelFactory
import com.vp.daggeraddons.ViewModelKey
import com.vp.favorites.FavoriteActivity
import com.vp.favorites.viewmodel.FavouriteViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import saha.tushar.common.di.DbModule

@Module
abstract class FavouriteViewModelModule {

    @Binds
    abstract fun bindDaggerViewModelFactory(daggerViewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FavouriteViewModel::class)
    abstract fun bindFavouriteViewModel(viewModel: FavouriteViewModel): ViewModel
}

@Module
abstract class FavouriteActivityModule {
    @ContributesAndroidInjector(modules = [FavouriteViewModelModule::class, DbModule::class])
    abstract fun bindFavouriteActivity(): FavoriteActivity
}