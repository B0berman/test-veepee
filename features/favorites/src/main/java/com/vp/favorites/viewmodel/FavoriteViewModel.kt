package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.model.FavoriteRepository
import com.vp.persistence.MovieEntity
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(repository: FavoriteRepository) : ViewModel() {

    private val favorites : LiveData<List<MovieEntity>> = repository.getFavoriteMovies()

    fun favorites() = favorites
}