package com.vp.favorites.model

import androidx.lifecycle.LiveData
import com.vp.persistence.MovieDao
import com.vp.persistence.MovieEntity
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val dao: MovieDao) {

    fun getFavoriteMovies(): LiveData<List<MovieEntity>> =
        dao.getFavoriteMovies()
}