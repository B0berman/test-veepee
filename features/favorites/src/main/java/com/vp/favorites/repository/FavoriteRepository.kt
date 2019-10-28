package com.vp.favorites.repository

import com.vp.favorites.model.BasicMovie
import io.reactivex.Completable
import io.reactivex.Single

interface FavoriteRepository {
    fun getAllMovies(): Single<List<BasicMovie>>
    fun addMovie(movie: BasicMovie) : Completable
    fun isMovieFav(movieID: String): Single<Boolean>
    fun deleteMovie(movieID: String) : Completable
}