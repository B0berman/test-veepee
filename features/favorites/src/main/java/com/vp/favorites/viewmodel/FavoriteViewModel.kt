package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.database.async.doAsync
import com.vp.database.beans.Movie
import com.vp.database.db.MovieDatabase
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val movieDatabase: MovieDatabase) : ViewModel() {
    private var movies : MutableLiveData<List<Movie>> = MutableLiveData()

    fun observeMovies() : LiveData<List<Movie>> = movies

    fun fetchFavorites() {
        doAsync {
            val favoriteMovies = movieDatabase.movieDao().allFavoriteMovies()
            movies.postValue(favoriteMovies)
        }.execute()
    }
}