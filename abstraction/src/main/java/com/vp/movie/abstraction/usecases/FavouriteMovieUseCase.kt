package com.vp.movie.abstraction.usecases

import com.vp.movie.abstraction.dto.Movie

interface FavouriteMovieUseCase {

    fun getFavouriteMovies()

    fun addMovieToFavourite(movie: Movie)
}