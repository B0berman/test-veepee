package com.vp.movie.domain.repositories

import com.vp.movie.abstraction.dto.Movie
import com.vp.movie.abstraction.repositories.FavouriteMovieRepository
import com.vp.movie.domain.ports.persistence.FavouriteMovieDaoPort
import io.reactivex.Observable
import io.reactivex.Single

class FavouriteMoviePersistenceAdapter(private val favouriteMovieDaoPort: FavouriteMovieDaoPort) : FavouriteMovieRepository {

    override fun getFavouriteMovies(): Observable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addMovieToFavourite(movie: Movie): Single<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}