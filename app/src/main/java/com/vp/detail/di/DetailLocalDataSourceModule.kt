package com.vp.detail.di

import com.vp.detail.data.DetailLocalDataSourceImpl
import com.vp.detail.datasource.DetailLocalDataSource
import com.vp.storage.MoviesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DetailLocalDataSourceModule {

    @Singleton
    @Provides
    fun providesDetailLocalDataSource(movieDataBase: MoviesDatabase): DetailLocalDataSource =
            DetailLocalDataSourceImpl(movieDataBase)
}
