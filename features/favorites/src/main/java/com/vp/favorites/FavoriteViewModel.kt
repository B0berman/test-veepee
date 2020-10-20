package com.vp.favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.favorite.FavoriteMovieRepository
import com.vp.favorite.model.Movie
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
) : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val movies = favoriteMovieRepository.getFavoriteMovies()
            _movies.value = movies
        }
    }

    fun remove(id: String) {
        viewModelScope.launch {
            favoriteMovieRepository.removeFavorite(id)
            refresh()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("FavoriteList", "onCleared:")
    }
}
