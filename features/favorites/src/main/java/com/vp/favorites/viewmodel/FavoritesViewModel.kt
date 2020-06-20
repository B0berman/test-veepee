package com.vp.favorites.viewmodel;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vp.data.local.FavoritesRepository
import com.vp.data.model.MovieFavorite
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(private val repository: FavoritesRepository): ViewModel() {

    private val favoritesLiveData = MediatorLiveData<List<MovieFavorite>>()

    fun favorites(): LiveData<List<MovieFavorite>> = favoritesLiveData

    init {
        favoritesLiveData.value = emptyList()
    }

    fun loadFavorites() {
        favoritesLiveData.addSource(repository.getAllLiveData()) {
            favoritesLiveData.value = it
        }
    }
}
