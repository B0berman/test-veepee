package com.vp.favorites.service

import androidx.lifecycle.LiveData
import com.vp.favorites.model.MovieItem

interface FavoriteService {
    fun observeMovieItems(): LiveData<MovieItem>
    fun observeMovieItemsList(): LiveData<List<MovieItem>>
    suspend fun refreshSelectedFavorite(imdbID: String)
    suspend fun saveMovieItem(movieItem: MovieItem)
    suspend fun deleteMovieItem(imdbID: String)


    suspend fun loadFavorites()
}