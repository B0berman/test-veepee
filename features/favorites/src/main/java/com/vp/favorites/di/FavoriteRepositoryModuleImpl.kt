package com.vp.favorites.di

import com.vp.favorites.data.db.FavoriteDao
import com.vp.favorites.data.repo.FavoriteRepositoryImpl
import com.vp.favorites.domain.repositories.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DaoModule::class])
internal class FavoriteRepositoryModuleImpl {

  @Singleton
  @Provides
  fun providesFavoriteRepository(favoriteDao: FavoriteDao)
      : FavoriteRepositoryImpl {
    return FavoriteRepositoryImpl(favoriteDao)
  }
}