package com.vp.movie.domain.usecases

import com.vp.movie.abstraction.dto.Movie
import com.vp.movie.abstraction.repositories.FavouriteMovieRepository
import com.vp.movie.abstraction.usecases.FavouriteMovieUseCase
import com.vp.movie.domain.ports.persistence.FavouriteMovieDaoPort

class FavouriteMovieUseCaseImp(private val favouriteMovieRepository: FavouriteMovieRepository):FavouriteMovieUseCase {

    override fun getFavouriteMovies() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addMovieToFavourite(movie: Movie) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}