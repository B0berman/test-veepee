package com.vp.movie.persistence.adapters

import com.vp.movie.abstraction.dto.Movie
import com.vp.movie.domain.ports.persistence.FavouriteMovieDaoPort
import com.vp.movie.persistence.dao.FavouriteMovieDao
import com.vp.movie.persistence.entities.MovieEntity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FavouriteMovieDaoAdapter(private val favouriteMovieDao: FavouriteMovieDao) : FavouriteMovieDaoPort {


    override fun getFavouriteMovies(): Observable<List<Movie>> {
        return favouriteMovieDao.getFavouriteMovies()
                .map { list -> list.map { it as Movie } }
                .subscribeOn(Schedulers.computation())
    }

    override fun addMovieToFavourite(movie: Movie): Single<Long> {
        return favouriteMovieDao.insertOrReplace(
                MovieEntity(
                        movie.title,
                        movie.year,
                        movie.imdbID,
                        movie.poster
                )
        ).subscribeOn(Schedulers.computation())
    }

    override fun getFavouriteMovieById(imdbID: String): Single<Movie> {
        return favouriteMovieDao.getFavouriteMovieById(imdbID)
                .map { it as Movie }
                .subscribeOn(Schedulers.computation())
    }

}