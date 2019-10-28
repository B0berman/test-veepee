package com.vp.favorites.di

import android.content.Context
import androidx.room.Room
import com.vp.favorites.framework.database.Database
import com.vp.favorites.framework.database.dao.MovieDao
import com.vp.favorites.repository.FavoriteRepository
import com.vp.favorites.repository.FavoriteRepositoryImpl
import com.vp.favorites.utils.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class FrameworkModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database =
        Room.databaseBuilder(
            context.applicationContext,
            Database::class.java, Database.DB_NAME
        ).build()

    @Provides
    @Singleton
    fun provideSchedulerProvider() : SchedulerProvider {
        return SchedulerProvider(Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    fun provideFavoriteRepository(favoriteDao: MovieDao): FavoriteRepository =
        FavoriteRepositoryImpl(favoriteDao)

    @Provides
    @Singleton
    fun provideFavoriteDao(database: Database): MovieDao = database.movieDao()
}