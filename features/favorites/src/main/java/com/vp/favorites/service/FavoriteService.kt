package com.vp.favorites.service

import com.vp.favorites.model.FavoriteMovie

interface FavoriteService {
    fun getFavoritesMovies(): List<FavoriteMovie>
}