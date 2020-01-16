package com.vp.list.model

import com.google.gson.annotations.SerializedName

class ListItem(
    @SerializedName("Title")
    val title: String? = null,
    @SerializedName("Year")
    val year: String? = null,
    @SerializedName("imdbID")
    val imdbID: String? = null,
    @SerializedName("Poster")
    val poster: String? = null
)