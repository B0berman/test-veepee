package com.vp.storage.di

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.vp.storage.MoviesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun providesSqliteDriver(context: Context): SqlDriver =
            AndroidSqliteDriver(MoviesDatabase.Schema, context, "vpDatabase.db")

    @Singleton
    @Provides
    fun providesFavoritesLocalDataSource(driver: SqlDriver): MoviesDatabase =
            MoviesDatabase(driver)
}
