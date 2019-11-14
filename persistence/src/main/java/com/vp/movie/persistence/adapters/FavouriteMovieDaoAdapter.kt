package com.vp.movie.persistence.adapters

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import com.vp.movie.abstraction.dto.Movie
import com.vp.movie.domain.ports.persistence.FavouriteMovieDaoPort
import com.vp.movie.persistence.dao.FavouriteMovieDao
import com.vp.movie.persistence.entities.MovieEntity

class FavouriteMovieDaoAdapter(private val favouriteMovieDao: FavouriteMovieDao) : FavouriteMovieDaoPort {


    override fun getFavouriteMovies(): LiveData<List<Movie>> {
        return map(favouriteMovieDao.getFavouriteMovies()) { list -> list.map { item -> item as Movie } }
    }

    override fun addMovieToFavourite(movie: Movie): LiveData<Long> {
        return favouriteMovieDao.insertOrReplaceMovie(
                MovieEntity(
                        movie.title,
                        movie.year,
                        movie.imdbID,
                        movie.poster
                )
        )
    }


}