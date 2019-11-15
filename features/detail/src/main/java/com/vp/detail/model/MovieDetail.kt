package com.vp.detail.model

import com.google.gson.annotations.SerializedName
import com.vp.movie.abstraction.dto.Movie

data class MovieDetail(@SerializedName("Title") override val title: String,
                       @SerializedName("Year") override val year: String,
                       @SerializedName("Runtime") val runtime: String,
                       @SerializedName("Director") val director: String,
                       @SerializedName("Plot") val plot: String,
                       @SerializedName("Poster") override val poster: String,
                       override val imdbID: String):Movie