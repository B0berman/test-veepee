package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.model.FavoriteMovie
import com.vp.storage.MoviesDao
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
        private val moviesDao: MoviesDao
) : ViewModel() {

    private val favoriteMovies: MutableLiveData<List<FavoriteMovie>> = MutableLiveData()

    fun favoriteMovies(): LiveData<List<FavoriteMovie>> = favoriteMovies

    fun fetchMovies() {
       moviesDao.getFavoriteMovies()
    }
}