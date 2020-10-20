package com.vp.favorite.db

import com.vp.favorite.db.data.MovieQueries
import com.vp.favorite.model.Movie

fun MovieQueries.insertMovie(movie: Movie) {
    insertMovie(
        movie.id,
        movie.title,
        movie.year,
        movie.runtime,
        movie.director,
        movie.plot,
        movie.posterUri.toString()
    )
}
