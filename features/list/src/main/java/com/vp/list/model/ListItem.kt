package com.vp.list.model

import com.vp.movies.data.model.MovieEntity

data class ListItem(
        val imdbID: String,
        val poster: String
) {

    constructor(entity: MovieEntity) : this(
            entity.imdbID,
            entity.poster ?: ""
    )
}

