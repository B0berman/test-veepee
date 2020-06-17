package com.vp.movies.di
import android.app.Application
import com.davidbragadeveloper.core.database.MoviesDatabase
import com.davidbragadeveloper.core.database.dao.MovieDao
import com.davidbragadeveloper.core.datasource.MovieslRoomDataSource
import com.vp.detail.localdatasource.DetailsLocalDataSource
import com.vp.favorites.localdatasource.FavoritesLocalDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {


    @Singleton
    @Provides
    fun providesRoomDatabase(application: Application): MoviesDatabase=
            MoviesDatabase.build(application.applicationContext)

    @Singleton
    @Provides
    fun providesMovieDao(database: MoviesDatabase): MovieDao = database.movieDao()

    @Singleton
    @Provides
    fun providesDetailRoomDataSource(detailRoomDataSource: MovieslRoomDataSource): DetailsLocalDataSource {
        return detailRoomDataSource
    }

    @Singleton
    @Provides
    fun providesFavoritesRoomDataSource(detailRoomDataSource: MovieslRoomDataSource): FavoritesLocalDataSource {
        return detailRoomDataSource
    }

}