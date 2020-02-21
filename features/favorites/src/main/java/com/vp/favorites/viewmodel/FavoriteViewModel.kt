package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.database.FavoriteMovieDatabase
import com.vp.favorites.model.FavoriteMovie
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val favoriteMovieDatabase: FavoriteMovieDatabase) : ViewModel() {

    private val favoriteMovies: MutableLiveData<List<FavoriteMovie>> = MutableLiveData()

    fun favoriteMovies(): LiveData<List<FavoriteMovie>> = favoriteMovies

    fun getFavoriteMovies() {
        val movies = favoriteMovieDatabase.getAll()
        favoriteMovies.postValue(movies)
    }

}
