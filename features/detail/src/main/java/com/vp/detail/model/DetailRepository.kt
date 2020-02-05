package com.vp.detail.model

import com.vp.persistence.MovieDao
import java.util.concurrent.Executors
import javax.inject.Inject

class DetailRepository @Inject constructor(private val dao: MovieDao) {

    fun saveMovie(movie: MovieDetail, isFavorite: Boolean) {
        Executors.newSingleThreadExecutor().execute {
            dao.saveMovie(movie.toEntity().apply { this.isFavorite = isFavorite })
        }
    }

    fun getMovieById(imdbId : String) = dao.getMovieById(imdbId)

}
