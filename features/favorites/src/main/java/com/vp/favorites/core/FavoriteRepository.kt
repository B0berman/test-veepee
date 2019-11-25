package com.vp.favorites.core

import com.vp.favorites.core.model.room.Movie
import io.reactivex.Completable
import io.reactivex.Single

interface FavoriteRepository {

    fun addMovieToFavorite(movie : Movie) : Completable

    fun getAllMovie() : Single<List<Movie>>

    fun removeFromFavorite(movieId : String) : Completable

    fun isMovieInFavorite(movieId : String) : Single<Boolean>
}