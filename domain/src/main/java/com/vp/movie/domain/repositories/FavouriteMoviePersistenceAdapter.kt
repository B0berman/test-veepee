package com.vp.movie.domain.repositories

import com.vp.movie.abstraction.dto.Movie
import com.vp.movie.abstraction.repositories.FavouriteMovieRepository
import com.vp.movie.domain.ports.persistence.FavouriteMovieDaoPort
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavouriteMoviePersistenceAdapter @Inject constructor(private val favouriteMovieDaoPort: FavouriteMovieDaoPort) : FavouriteMovieRepository {

    override fun getFavouriteMovies(): Observable<List<Movie>> {
        return favouriteMovieDaoPort.getFavouriteMovies()
    }

    override fun addMovieToFavourite(movie: Movie): Single<Long> {
        return favouriteMovieDaoPort.addMovieToFavourite(movie)
    }

    override fun getFavouriteMovieById(imdbID: String): Single<Movie> {
        return favouriteMovieDaoPort.getFavouriteMovieById(imdbID)
    }
}