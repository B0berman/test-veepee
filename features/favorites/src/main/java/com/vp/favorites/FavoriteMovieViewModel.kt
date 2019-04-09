package com.vp.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.vp.databases.FavoriteMovieRepository
import com.vp.databases.model.FavoriteMovie

class FavoriteMovieViewModel(application: Application) : AndroidViewModel(application) {

    var repository: FavoriteMovieRepository = FavoriteMovieRepository(application)
    var favoriteMovieList: LiveData<List<FavoriteMovie>>

    init {
        favoriteMovieList = repository.favoriteMovieList
    }

    fun getListFavorite(): LiveData<List<FavoriteMovie>> {
        return favoriteMovieList
    }
}