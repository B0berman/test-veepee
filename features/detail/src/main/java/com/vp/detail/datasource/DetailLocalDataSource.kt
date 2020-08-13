package com.vp.detail.datasource

import androidx.lifecycle.LiveData
import com.vp.detail.model.MovieDetail

interface DetailLocalDataSource {
    fun setAsFavorite(imdbID: String, movieDetail: MovieDetail)
    fun removeAsFavorite(imdbID: String)
    fun getDetail(imdbID: String): MovieDetail?
    fun isFavorite(imdbID: String): LiveData<Boolean>
}
