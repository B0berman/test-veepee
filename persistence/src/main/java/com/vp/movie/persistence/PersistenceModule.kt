package com.vp.movie.persistence

import android.app.Application
import androidx.room.Room
import com.vp.movie.domain.ports.persistence.FavouriteMovieDaoPort
import com.vp.movie.persistence.DatabaseMetaData.NAME
import com.vp.movie.persistence.adapters.FavouriteMovieDaoAdapter
import com.vp.movie.persistence.dao.FavouriteMovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class PersistenceModule {

    @Singleton
    @Provides
    fun providesRoom(application: Application): MovieDatabase =
            Room.databaseBuilder(
                    application,
                    MovieDatabase::class.java,
                    NAME
            ).build()

    @Singleton
    @Provides
    fun providesFavouriteMovies(database: MovieDatabase) = database.favouritesDao()

    @Singleton
    @Provides
    fun favouriteMoviePort(favouriteMovieDao: FavouriteMovieDao): FavouriteMovieDaoPort =
            FavouriteMovieDaoAdapter(favouriteMovieDao)

}
