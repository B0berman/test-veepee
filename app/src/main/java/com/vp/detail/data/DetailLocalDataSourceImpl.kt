package com.vp.detail.data

import androidx.lifecycle.LiveData
import com.vp.detail.datasource.DetailLocalDataSource
import com.vp.detail.model.MovieDetail
import com.vp.storage.MoviesDatabase
import com.vp.storage.db.asLiveData
import com.vp.storage.db.mapToOne

class DetailLocalDataSourceImpl(
        private val moviesDatabase: MoviesDatabase
) : DetailLocalDataSource {
    override fun setAsFavorite(imdbID: String, movieDetail: MovieDetail) {
        moviesDatabase.moviesQueries.insertOrReplace(movieDetail.toDb(imdbID))
    }

    override fun removeAsFavorite(imdbID: String) {
        moviesDatabase.moviesQueries.deleteById(imdbID)
    }

    override fun getDetail(imdbID: String): MovieDetail? =
            moviesDatabase.moviesQueries.selectById(imdbID).executeAsOneOrNull()
                    ?.toMovieDetail()

    override fun isFavorite(imdbID: String): LiveData<Boolean> =
            moviesDatabase.moviesQueries.exist(imdbID).asLiveData()
                    .mapToOne { it }
}
