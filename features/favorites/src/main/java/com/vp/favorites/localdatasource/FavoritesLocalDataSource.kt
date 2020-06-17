package com.vp.favorites.localdatasource

import com.vp.favorites.model.MovieFavorite

interface FavoritesLocalDataSource {
    fun loadFavorites(): List<MovieFavorite>
}