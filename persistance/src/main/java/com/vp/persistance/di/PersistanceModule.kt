package com.vp.persistance.di

import android.content.Context
import androidx.room.Room
import com.vp.persistance.MovieDB
import com.vp.persistance.MovieDataSource
import com.vp.persistance.RepositoryDB
import com.vp.persistance.dao.FavoriteMovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Uxio Lorenzo on 2019-09-09.
 */
@Module
class PersistanceModule {

    @Singleton
    @Provides
    fun providesMovieDB(context: Context): MovieDB
            = Room.databaseBuilder(context, MovieDB::class.java,"movie-db").build()

    @Singleton
    @Provides
    fun providesFavoriteMovieDao(movieDB: MovieDB): FavoriteMovieDao
            = movieDB.favoriteMovieDao()


    @Singleton
    @Provides
    fun providesRepositoryDB(favoriteMovieDao: FavoriteMovieDao): RepositoryDB
            = MovieDataSource(favoriteMovieDao)

}