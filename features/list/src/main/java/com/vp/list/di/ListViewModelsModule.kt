package com.vp.list.di

import androidx.lifecycle.ViewModel
import com.vp.core.di.ViewModelFactoryModule
import com.vp.core.di.ViewModelKey
import com.vp.list.viewmodel.ListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class])
abstract class ListViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindListViewModel(listViewModel: ListViewModel): ViewModel
}