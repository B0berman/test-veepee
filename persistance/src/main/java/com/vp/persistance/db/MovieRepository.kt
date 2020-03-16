/*
 * Created by Alexis Rodriguez Paret on 3/13/20 8:58 PM
 * Copyright (c) 2020. All rights reserved. Heliocor (alexis.rodriquez@heliocor.com)
 * Last modified 3/13/20 8:58 PM
 *
 */

package com.vp.persistance.db

import androidx.lifecycle.LiveData
import com.vp.persistance.dao.MovieDao
import com.vp.persistance.model.MovieModel

/**
 * Created by Alexis Rodríguez Paret on 2020-03-13.
 */
class MovieRepository(private val movieDao: MovieDao) {

    val allMovies: LiveData<List<MovieModel>> = movieDao.getAll()

    suspend fun insert(movie: MovieModel) {
        movieDao.insert(movie)
    }

    fun observeMovieByID(movieId: String): LiveData<MovieModel> {
        return movieDao.getByimdbID(movieId)
    }

    suspend fun remove(movieId: String) {
        movieDao.delete(movieId)
    }


}