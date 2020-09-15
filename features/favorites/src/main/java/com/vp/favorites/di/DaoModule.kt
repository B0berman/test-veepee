package com.vp.favorites.di

import com.vp.favorites.data.db.AppDB
import com.vp.favorites.data.db.FavoriteDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaoModule {

  @Singleton
  @Provides
  fun providesfavoriteDao(): FavoriteDao {
    return AppDB.getInstance().favoriteDao()
  }
}