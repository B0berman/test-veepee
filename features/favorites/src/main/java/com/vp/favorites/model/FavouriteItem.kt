package com.vp.favorites.model

import com.vp.movie.abstraction.dto.Movie

data class FavouriteItem(
        override val title: String,
        override val year: String,
        override val imdbID: String,
        override val poster: String
) : Movie