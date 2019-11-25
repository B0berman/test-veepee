package com.vp.favorites.core

import com.vp.favorites.core.model.room.Movie
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


class FavoriteInteractor @Inject constructor(private val favoriteRepository: FavoriteRepository) {


    fun addMovieToFavorite(movie : Movie) : Completable = favoriteRepository.addMovieToFavorite(movie)


    fun getAllMovie() : Single<List<Movie>> = favoriteRepository.getAllMovie()

    fun removeFromFavorite(movieId : String) : Completable = favoriteRepository.removeFromFavorite(movieId)


    fun isMovieInFavorite(movieId : String) : Single<Boolean> = favoriteRepository.isMovieInFavorite(movieId)


}