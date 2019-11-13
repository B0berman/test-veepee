package com.vp.movies.data.model

import com.google.gson.annotations.SerializedName

class SearchResultEntity<T : Any>(
    @SerializedName("Response")
    val response: String? = null,
    @SerializedName("Search")
    val search: List<T>? = null,
    @SerializedName("totalResults")
    val totalResults: Int = 0
)
