package com.vp.detail.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MovieDetail(@SerializedName("Title") val title: String,
                       @SerializedName("Year") val year: String,
                       @SerializedName("Runtime") val runtime: String,
                       @SerializedName("Director") val director: String,
                       @SerializedName("Plot") val plot: String,
                       @SerializedName("Poster") val poster: String)