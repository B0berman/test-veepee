package com.vp.favorites.model

import com.vp.movies.data.model.MovieEntity

data class FavoriteListItem(
        val imdbID: String,
        val poster: String
) {

    constructor(entity: MovieEntity) : this(
            entity.imdbID,
            entity.poster ?: ""
    )
}

