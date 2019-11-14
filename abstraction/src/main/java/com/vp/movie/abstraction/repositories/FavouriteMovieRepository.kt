package com.vp.movie.abstraction.repositories

import com.vp.movie.abstraction.dto.Movie
import io.reactivex.Observable
import io.reactivex.Single

interface FavouriteMovieRepository {

    fun getFavouriteMovies(): Observable<List<Movie>>

    fun addMovieToFavourite(movie: Movie): Single<Long>
}