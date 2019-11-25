package com.vp.favorites.di.module

import android.app.Application
import androidx.room.Room
import com.vp.favorites.core.FavoriteRepository
import com.vp.favorites.data.repository.FavoriteRepositoryImpl
import com.vp.favorites.data.room.FavoriteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [FavoriteFragmentModule::class, FavoriteActivityModule::class])
class FavoriteModule {

    @Singleton
    @Provides
    fun provideDataBase(application: Application) : FavoriteDatabase {
        return Room.databaseBuilder(application, FavoriteDatabase::class.java, "favorite-database").build()
    }

    @Singleton
    @Provides
    fun bindFavoriteRepository(favoriteRepositoryImpl: FavoriteRepositoryImpl) : FavoriteRepository = favoriteRepositoryImpl


}