package com.vp.core.favorites

import androidx.lifecycle.LiveData

interface FavoriteMoviesRepository {

    fun isFavorite(movieId: String): LiveData<Boolean>

    suspend fun setFavorite(movie: Movie, favorite: Boolean)

    data class Movie(
            val id: String,
            val title: String,
            val year: String,
            val director: String,
            val poster: String?
    )
}