package com.vp.movie.domain.model

import com.vp.movie.abstraction.dto.Movie

data class FavouriteMovie(
        override val title: String,
        override val year: String,
        override val imdbID: String,
        override val poster: String) : Movie