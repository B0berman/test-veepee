package com.vp.detail.model

import com.vp.movies.data.model.MovieEntity

data class MovieDetail(
        val title: String,
        val year: String,
        val runtime: String,
        val director: String,
        val plot: String,
        val poster: String
) {

    constructor(entity: MovieEntity) : this(
            entity.title ?: "",
            entity.year ?: "",
            entity.runtime ?: "",
            entity.director ?: "",
            entity.plot ?: "",
            entity.poster ?: ""
    )
}