package com.vp.movies.di
import android.app.Application
import com.davidbragadeveloper.core.MoviesDatabase
import com.davidbragadeveloper.core.dao.MovieDao
import com.vp.movies.MoviesApplication
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class RoomModule @Inject constructor(private val application: Application) {

    @Singleton
    @Provides
    fun providesRoomDatabase(): MoviesDatabase = MoviesDatabase.build(application.applicationContext)

    @Singleton
    @Provides
    fun providesMovieDao(moviesDatabase: MoviesDatabase): MovieDao = moviesDatabase.movieDao()

}