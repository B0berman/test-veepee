package com.vp.moviedatabase.di

import android.app.Application
import com.vp.moviedatabase.data.AppDatabase
import com.vp.moviedatabase.data.FavoriteMovieDao
import com.vp.moviedatabase.data.FavoriteMovieLogic
import dagger.Module
import dagger.Provides

@Module
class MovieDatabaseModule {

    @Provides
    fun providesDatabase(application: Application):AppDatabase {
        return AppDatabase.getInstance(application)
    }

    @Provides
    fun providesFavoriteMovieDao(appDatabase: AppDatabase):FavoriteMovieDao {
        return appDatabase.favoriteMovieDao()
    }

    @Provides
    fun providesFavoriteMovieLogic(dao: FavoriteMovieDao):FavoriteMovieLogic {
        return FavoriteMovieLogic(dao)
    }
}