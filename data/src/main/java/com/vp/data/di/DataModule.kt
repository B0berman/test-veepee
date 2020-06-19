package com.vp.data.di

import com.vp.data.local.FavoritesRepository
import com.vp.data.local.FavoritesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module(includes = [RoomModule::class])
abstract class DataModule {

    @Binds
    abstract fun bindFavoritesRepository(repositoryImpl: FavoritesRepositoryImpl): FavoritesRepository
}
