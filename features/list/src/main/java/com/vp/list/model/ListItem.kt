package com.vp.list.model

import com.google.gson.annotations.SerializedName

open class ListItem(
    @SerializedName("Title")
    val title: String?,
    @SerializedName("Year")
    val year: String?,
    @SerializedName("imdbID")
    // ANSWER after shrink imdbID name is changed so gson is not able to set this value.
    val imdbID: String?,
    @SerializedName("Poster")
    val poster: String?
)