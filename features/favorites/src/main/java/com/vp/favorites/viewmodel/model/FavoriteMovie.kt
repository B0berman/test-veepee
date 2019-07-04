package com.vp.favorites.viewmodel.model

internal data class FavoriteMovie(
        val id: String,
        val title: String,
        val year: String,
        val director: String,
        val poster: String?
)