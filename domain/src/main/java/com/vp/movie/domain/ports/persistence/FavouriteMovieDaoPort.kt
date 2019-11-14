package com.vp.movie.domain.ports.persistence

import androidx.lifecycle.LiveData
import com.vp.movie.abstraction.dto.Movie
import io.reactivex.Observable
import io.reactivex.Single

interface FavouriteMovieDaoPort {

    fun getFavouriteMovies(): Observable<List<Movie>>

    fun addMovieToFavourite(movie: Movie): Single<Long>
}
