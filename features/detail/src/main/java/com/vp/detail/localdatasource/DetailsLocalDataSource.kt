package com.vp.detail.localdatasource

import com.vp.detail.model.MovieDetail

interface DetailsLocalDataSource {

    fun insertMovie(movie: MovieDetail): Boolean
    fun updateMovie(movie: MovieDetail): Boolean
    fun getMovieById(movieId: String) : MovieDetail?

}