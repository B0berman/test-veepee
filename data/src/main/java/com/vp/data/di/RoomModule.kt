package com.vp.data.di

import android.content.Context
import com.vp.data.local.FavoritesDao
import com.vp.data.local.MoviesDatabase
import dagger.Module
import dagger.Provides

@Module
class RoomModule {

    @Provides
    fun provideFavoritesDao(moviesDatabase: MoviesDatabase): FavoritesDao {
        return moviesDatabase.movieDao()
    }

    @Provides
    fun provideMoviesDatabase(context: Context): MoviesDatabase {
        return MoviesDatabase.getInstance(context)
    }
}
