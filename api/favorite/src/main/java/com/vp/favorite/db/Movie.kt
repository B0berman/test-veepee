package com.vp.favorite.db

import android.net.Uri
import com.vp.favorite.model.Movie
import com.vp.favorite.db.data.Movie as MovieEntity

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        year = year,
        runtime = runtime,
        director = director,
        plot = plot,
        posterUri = Uri.parse(posterUri)
    )
}
