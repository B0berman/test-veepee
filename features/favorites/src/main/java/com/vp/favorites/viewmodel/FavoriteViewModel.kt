package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.vp.database.entity.FavoriteEntity
import com.vp.favorites.repository.FavoriteRepository
import javax.inject.Inject


class FavoriteViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private val mRepository: FavoriteRepository = FavoriteRepository(application)
    private var favoriteMovies : LiveData<List<FavoriteEntity>>?

    init {
        favoriteMovies = mRepository.getAll()
    }

    fun insert(movie : FavoriteEntity) {
        mRepository.insert(movie)
    }

    fun getAll() : LiveData<List<FavoriteEntity>>? {
        return favoriteMovies
    }
}