package com.vp.movie.domain.ports.persistence

import androidx.lifecycle.LiveData
import com.vp.movie.abstraction.dto.Movie

interface FavouriteMovieDaoPort {

    fun getFavouriteMovies(): LiveData<List<Movie>>

    fun addMovieToFavourite(movie: Movie): LiveData<Long>
}