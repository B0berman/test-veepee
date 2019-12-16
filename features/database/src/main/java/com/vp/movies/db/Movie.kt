package com.vp.movies.db

data class Movie(
        val title: String,
        val year: String?,
        val runtime: String?,
        val director: String?,
        val plot: String?,
        val poster: String?
)