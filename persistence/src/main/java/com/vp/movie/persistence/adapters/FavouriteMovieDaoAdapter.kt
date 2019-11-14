package com.vp.movie.persistence.adapters

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import com.vp.movie.abstraction.dto.Movie
import com.vp.movie.domain.ports.persistence.FavouriteMovieDaoPort
import com.vp.movie.persistence.dao.FavouriteMovieDao
import com.vp.movie.persistence.entities.MovieEntity

class FavouriteMovieDaoAdapter(private val favouriteMovieDao: FavouriteMovieDao) : FavouriteMovieDaoPort {


    override fun getFavouriteMovies(): List<Movie> {
        return favouriteMovieDao.getFavouriteMovies().map { item -> item as Movie }
    }

    override fun addMovieToFavourite(movie: Movie): Long {
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