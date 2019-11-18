package com.vp.movie.domain.usecases

import com.vp.movie.abstraction.dto.Movie
import com.vp.movie.abstraction.repositories.FavouriteMovieRepository
import com.vp.movie.abstraction.usecases.FavouriteMovieUseCase
import com.vp.movie.domain.ports.persistence.FavouriteMovieDaoPort
import io.reactivex.Observable
import io.reactivex.Single

class FavouriteMovieUseCaseImp(private val favouriteMovieRepository: FavouriteMovieRepository) : FavouriteMovieUseCase {

    override fun getFavouriteMovies(): Observable<List<Movie>> {
        return favouriteMovieRepository.getFavouriteMovies()
    }

    override fun addMovieToFavourite(movie: Movie): Single<Long> {
        return favouriteMovieRepository.addMovieToFavourite(movie)
    }

    override fun getFavouriteMovieById(imdbID: String): Single<Movie> {
        return favouriteMovieRepository.getFavouriteMovieById(imdbID)
    }

    override fun removeMovieToFavouriteById(imdbID: String): Single<Int> {
        return favouriteMovieRepository.removeMovieToFavouriteById(imdbID)
    }
}