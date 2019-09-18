package com.vp.detail.mapper

import com.vp.detail.model.MovieDetail
import com.vp.persistance.model.FavoriteMovie

/**
 * Created by Uxio Lorenzo on 2019-09-10.
 */
class MovieToFavoriteMovieMapper: Mapper<MovieDetail, FavoriteMovie> {

    override fun map(from: MovieDetail): FavoriteMovie
            = FavoriteMovie(omdbId = "",
                year = from.year,
                title = from.title,
                poster = from.poster,
                director = from.director,
                plot = from.plot,
                runtime = from.runtime)

    fun map(omdbId: String, from: MovieDetail): FavoriteMovie
            = FavoriteMovie(omdbId = omdbId,
                year = from.year,
                title = from.title,
                poster = from.poster,
                director = from.director,
                plot = from.plot,
                runtime = from.runtime)

}