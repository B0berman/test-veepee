package com.vp.movies.di

import android.content.Context
import androidx.room.Room
import com.vp.movies.data.local.AppDatabase
import com.vp.movies.data.local.dao.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocalModule {

    @JvmStatic
    @Provides
    @Singleton
    internal fun appDatabase(
        context: Context
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "app_database.db")
        .fallbackToDestructiveMigration()
        .build()

    @JvmStatic
    @Provides
    @Singleton
    internal fun movieDao(
        database: AppDatabase
    ): MovieDao = database.movieDao()
}
