package com.vp.movie.abstraction.usecases

import com.vp.movie.abstraction.dto.Movie
import io.reactivex.Observable
import io.reactivex.Single

interface FavouriteMovieUseCase {

    fun getFavouriteMovies(): Observable<List<Movie>>

    fun addMovieToFavourite(movie: Movie): Single<Long>

    fun getFavouriteMovieById(imdbID: String): Single<Movie>
}