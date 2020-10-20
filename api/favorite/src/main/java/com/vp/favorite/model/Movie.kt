package com.vp.favorite.model

import android.net.Uri

data class Movie(
    val id: String,
    val title: String,
    val year: String,
    val runtime: String,
    val director: String,
    val plot: String,
    val posterUri: Uri
)
