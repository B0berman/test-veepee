package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.model.FavoriteMovie
import com.vp.moviedatabase.data.FavoriteMovieLogic
import com.vp.moviedatabase.data.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteMovieViewModel @Inject constructor(private val favoriteMovieLogic: FavoriteMovieLogic):ViewModel()  {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val favoritesLiveData = MutableLiveData<List<FavoriteMovie>> ()

    fun favorites(): LiveData<List<FavoriteMovie>> {
        return favoritesLiveData
    }

    fun requestFavorites() {
        uiScope.launch {
            favoriteMovieLogic.getAll()
            val favoriteMovies = favoriteMovieLogic.getAll().map { it.toFavoriteMovie() }
            favoritesLiveData.postValue(favoriteMovies)
        }
    }



}

fun Movie.toFavoriteMovie():FavoriteMovie {
    return FavoriteMovie(title,director,posterId)
}