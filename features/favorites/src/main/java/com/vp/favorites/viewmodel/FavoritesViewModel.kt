package com.vp.favorites.viewmodel;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.data.local.FavoritesRepository
import com.vp.data.model.MovieFavorite
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(private val repository: FavoritesRepository): ViewModel() {

    private val favoritesLiveData = MutableLiveData<List<MovieFavorite>>()

    fun favorites(): LiveData<List<MovieFavorite>> = favoritesLiveData

    init {
        favoritesLiveData.value = emptyList()
    }

    fun loadFavorites() {
        Thread(Runnable{
            val list = repository.getAll()
            favoritesLiveData.postValue(list)
        }).start()
    }
}
