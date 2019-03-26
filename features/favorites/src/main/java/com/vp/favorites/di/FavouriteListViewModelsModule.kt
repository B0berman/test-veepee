package com.vp.list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vp.daggeraddons.DaggerViewModelFactory
import com.vp.daggeraddons.ViewModelKey
import com.vp.favorites.viewmodel.FavouriteListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FavouriteListViewModelsModule {

    @Binds
    abstract fun bindDaggerViewModelFactory(daggerViewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FavouriteListViewModel::class)
    abstract fun bindFavouriteListViewModel(favouriteListViewModel: FavouriteListViewModel): ViewModel
}