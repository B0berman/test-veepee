package com.vp.favorites.model

import com.google.gson.annotations.SerializedName

data class FavoriteMovie(
        @SerializedName ("movieId") val movieId: String,
        @SerializedName ("title") val title: String,
        @SerializedName ("cover") val cover: String
) {
    fun isValid() = movieId.isNotBlank() && title.isNotBlank() && cover.isNotBlank() && cover != "N/A"
}