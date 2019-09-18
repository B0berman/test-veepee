package com.vp.detail.di

import com.vp.detail.mapper.MovieToFavoriteMovieMapper
import dagger.Module
import dagger.Provides

/**
 * Created by Uxio Lorenzo on 2019-09-10.
 */
@Module
class DetailModelModule {

    @Provides
    fun providesMovieToFavoriteMovieMapper(): MovieToFavoriteMovieMapper
            = MovieToFavoriteMovieMapper()

}