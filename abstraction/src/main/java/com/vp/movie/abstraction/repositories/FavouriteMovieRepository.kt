package com.vp.movie.abstraction.repositories

import com.vp.movie.abstraction.dto.Movie

interface FavouriteMovieRepository {

    fun getFavouriteMovies()

    fun addMovieToFavourite(movie: Movie)
}