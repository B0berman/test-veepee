package com.vp.favorites.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vp.favorites.SharedPreferencesManager
import com.vp.favorites.model.FavoriteMovie
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(private val sharedPreferencesManager: SharedPreferencesManager): FavoriteRepository {

    override fun isFavorite(movieId: String): Boolean {
        return getMovieListFromString().any { it.movieId == movieId }
    }

    override fun onFavoriteSelection(favoriteMovie: FavoriteMovie): Boolean {
        val favoriteMovieList = getMovieListFromString()
        return if (favoriteMovieList.contains(favoriteMovie)) {
            saveFavoriteString(favoriteMovieList - favoriteMovie)
            false
        } else {
            saveFavoriteString(favoriteMovieList + favoriteMovie)
            true
        }
    }

    override fun getFavorites(): List<FavoriteMovie> {
        return getMovieListFromString()
    }

    private fun saveFavoriteString(favoriteList: List<FavoriteMovie>) {
        sharedPreferencesManager.saveFavoriteString(Gson().toJson(favoriteList))
    }

    private fun getMovieListFromString(): List<FavoriteMovie> {
        val listType = TypeToken.getParameterized(java.util.ArrayList::class.java, FavoriteMovie::class.java).type
        Gson().fromJson<ArrayList<FavoriteMovie>>(sharedPreferencesManager.getFavoriteString(), listType)?.let {
            return it
        } ?: return emptyList()
    }
}