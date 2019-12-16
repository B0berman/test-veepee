package com.vp.favorites.viewmodel

import androidx.lifecycle.ViewModel
import com.vp.movies.db.Movie
import com.vp.movies.db.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesActivityModel @Inject constructor(private val movieDatabase: MovieDatabase) : ViewModel() {

    fun onCreate() {

    }

    fun loadMovies(callback: (List<Movie>, Throwable?) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val movies: List<Movie> = movieDatabase.getFavoriteMovies()

            GlobalScope.launch(Dispatchers.Main) {
                callback(movies, null)
            }
        }
    }

    fun onDestroy() {

    }
}