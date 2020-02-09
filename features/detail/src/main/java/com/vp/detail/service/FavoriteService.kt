package com.vp.detail.service

import com.vp.detail.model.MovieDetail

interface FavoriteService {

    fun isFavorite(movieId: String): Boolean

    fun saveToFavorites(movieId: String, movie: MovieDetail)

    fun removeFromFavorites(movieId: String)
}
