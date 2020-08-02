package com.vp.favorites.model

data class FavoriteMovie(
        val movieId: String,
        val title: String,
        val cover: String
) {
    fun isValid() = movieId.isNotBlank() && title.isNotBlank() && cover.isNotBlank() && cover != "N/A"
}