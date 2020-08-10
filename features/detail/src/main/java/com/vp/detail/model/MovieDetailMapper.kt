package com.vp.detail.model

import com.vp.storage.model.MovieItemDTO
import javax.inject.Inject

class MovieDetailMapper @Inject constructor(){

    fun toMovieItemDTO(imdbID : String, movieDetail: MovieDetail) : MovieItemDTO = MovieItemDTO(0, imdbID, movieDetail.title, movieDetail.year, movieDetail.poster)
}