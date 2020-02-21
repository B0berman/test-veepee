package com.vp.favorites.di

import com.vp.favorites.database.DiskFavoriteMovieDatabase
import com.vp.favorites.database.FavoriteMovieDatabase
import dagger.Binds
import dagger.Module

/**
 * Created by Albert Vila Calvo on 2020-02-20.
 */
@Module
abstract class FavoriteMovieDatabaseModule {

    @Binds
    abstract fun providesMovieDatabase(diskFavoriteMovieDatabase: DiskFavoriteMovieDatabase): FavoriteMovieDatabase

}
