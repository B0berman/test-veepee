package com.vp.detail.data

import com.vp.detail.model.MovieDetail
import com.vp.storage.dbmodel.DbMovie

fun MovieDetail.toDb(imdbId: String): DbMovie = DbMovie(
        imdbID = imdbId,
        title = title,
        year = year,
        runtime = runtime,
        director = director,
        plot = plot,
        poster = poster
)

fun DbMovie.toMovieDetail(): MovieDetail = MovieDetail(
        title = title,
        year = year,
        runtime = runtime,
        director = director,
        plot = plot,
        poster = poster
)
