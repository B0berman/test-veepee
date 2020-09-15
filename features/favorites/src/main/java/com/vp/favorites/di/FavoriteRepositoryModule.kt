package com.vp.favorites.di

import com.vp.favorites.data.repo.FavoriteRepositoryImpl
import com.vp.favorites.domain.repositories.FavoriteRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [FavoriteRepositoryModuleImpl::class])
abstract class FavoriteRepositoryModule {

  @Singleton
  @Binds
  abstract fun bindsFavoriteRepository(favoriteRepositoryImpl: FavoriteRepositoryImpl): FavoriteRepository

}