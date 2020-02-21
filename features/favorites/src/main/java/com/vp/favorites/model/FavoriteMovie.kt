package com.vp.favorites.model

import androidx.annotation.Keep


@Keep
data class FavoriteMovie(
        val id: String,
        val title: String,
        val year: String
)
