package com.vp.favorites.di

import androidx.lifecycle.ViewModel
import com.vp.core.di.ViewModelFactoryModule
import com.vp.core.di.ViewModelKey
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class])
abstract class FavoriteViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    internal abstract fun bindFavoriteViewModel(viewModel: FavoriteViewModel): ViewModel
}