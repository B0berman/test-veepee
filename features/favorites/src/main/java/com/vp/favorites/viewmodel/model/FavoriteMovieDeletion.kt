package com.vp.favorites.viewmodel.model

internal data class FavoriteMovieDeletion(
        val movie: FavoriteMovie,
        val undo: () -> Unit,
        val dismiss: () -> Unit
)