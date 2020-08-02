package com.vp.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.model.FavoriteMovie
import com.vp.favorites.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val favoriteRepository: FavoriteRepository): ViewModel() {

    private val movieList: MutableLiveData<List<FavoriteMovie>> = MutableLiveData()

    fun movieList(): LiveData<List<FavoriteMovie>> = movieList

    fun fetchMovieList() {
        movieList.value = favoriteRepository.getFavorites().filter { it.isValid() }
    }
}