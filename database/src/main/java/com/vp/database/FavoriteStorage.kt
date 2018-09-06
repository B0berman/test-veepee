package com.vp.database

import android.arch.lifecycle.LiveData
import com.vp.database.model.FavoriteMovie

interface FavoriteStorage {
    suspend fun insertFavorite(favoriteMovie: FavoriteMovie): Long

    suspend fun removeFavorite(movieId: String): Int

    fun getFavoriteMovies(): LiveData<List<FavoriteMovie>>

    suspend fun containsMovie(movieId: String): Boolean
}