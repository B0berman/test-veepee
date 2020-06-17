package com.vp.favorites.viewmodel

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.localdatasource.FavoritesLocalDataSource
import com.vp.favorites.model.MovieFavorite
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
        private val favoritesLocalDataSource: FavoritesLocalDataSource
) : ViewModel(){

    private val favorites: MutableLiveData<List<MovieFavorite>> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun favorites(): LiveData<List<MovieFavorite>> = favorites
    fun state(): LiveData<LoadingState> = loadingState


    fun fetchFavorites() {
        loadingState.value = LoadingState.IN_PROGRESS
        // I use a AsyncTask just because coroutines are experimental and I don´t know if
        // libraries are allowed like the reactive ones, I now other methods like threading
        // but I think async task is better when no observables or suspend functions are allowed
        AsyncTask.execute {
            try {
                val movies = favoritesLocalDataSource.loadFavorites()
                favorites.postValue(movies)
                loadingState.postValue(LoadingState.LOADED)
            }catch (e: Exception) {
                loadingState.postValue(LoadingState.ERROR)
            }
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }

}