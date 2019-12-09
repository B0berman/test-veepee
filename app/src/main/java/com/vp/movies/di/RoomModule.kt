package com.vp.movies.di

import android.app.Application
import androidx.room.Room
import com.gmail.saneme87.roomdatabase.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideAppDatabase(app: Application) = Room.databaseBuilder(
            app.applicationContext,
            AppDatabase::class.java, "app_database"
    ).build()

    @Singleton
    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase) = appDatabase.favoriteDao()

}