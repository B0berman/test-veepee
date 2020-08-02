package com.vp.favorites.repository

import com.vp.favorites.model.FavoriteMovie

interface FavoriteRepository {
    fun isFavorite(movieId: String): Boolean
    fun onFavoriteSelection(favoriteMovie: FavoriteMovie): Boolean
    fun getFavorites(): List<FavoriteMovie>
}