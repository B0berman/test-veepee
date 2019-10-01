package com.vp.favorites.di

import android.app.Application
import androidx.room.Room
import com.vp.database.AppDatabase
import com.vp.database.dao.FavoriteDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FavoriteDatabaseModule {

    @Provides
    @Singleton
    internal fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application.applicationContext,
                AppDatabase::class.java, "veepee_movies")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    @Singleton
    internal fun provideMovieDao(appDatabase: AppDatabase): FavoriteDao {
        return appDatabase.favoriteDao()
    }
}