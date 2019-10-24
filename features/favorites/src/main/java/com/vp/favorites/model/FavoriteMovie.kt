package com.vp.favorites.model

data class FavoriteMovie (
        val title: String,
        val year: String,
        val runtime: String,
        val director: String,
        val plot: String,
        val poster: String?,
        val imdbID: String
)