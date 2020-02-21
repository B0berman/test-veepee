package com.vp.favorites.database

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vp.favorites.model.FavoriteMovie
import javax.inject.Inject

private const val PREFS_KEY = "favorite_movies_list"

/**
 * Created by Albert Vila Calvo on 2020-02-20.
 */
class DiskFavoriteMovieDatabase @Inject constructor(application: Application) : FavoriteMovieDatabase {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    private val gson: Gson = Gson()

    override fun getAll(): List<FavoriteMovie> {
        val serializedMovies = sharedPreferences.getString(PREFS_KEY, "")
        val itemType = object : TypeToken<List<FavoriteMovie>>() {}.type
        val movies: List<FavoriteMovie>? = gson.fromJson<List<FavoriteMovie>>(serializedMovies, itemType)
        return movies ?: emptyList()
    }

    override fun add(favoriteMovie: FavoriteMovie) {
        val movies = getAll().toMutableList()
        movies.add(favoriteMovie)
        saveMovies(movies)
    }

    override fun remove(movieId: String) {
        val movies = getAll().toMutableList()
        val favoriteMovie = movies.find { it.id == movieId } ?: return
        movies.remove(favoriteMovie)
        saveMovies(movies)
    }

    override fun contains(movieId: String): Boolean {
        return getAll().find { it.id == movieId } != null
    }

    private fun saveMovies(movies: List<FavoriteMovie>) {
        val serializedMovies = gson.toJson(movies)
        with(sharedPreferences.edit()) {
            putString(PREFS_KEY, serializedMovies)
            apply()
        }
    }

}
