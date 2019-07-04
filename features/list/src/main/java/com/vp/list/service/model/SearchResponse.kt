package com.vp.list.service.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
        @SerializedName("Search") val items: List<Item>?,
        @SerializedName("totalResults") val totalResults: Int?
) {

    data class Item(
            @SerializedName("Title") val title: String?,
            @SerializedName("Year") val year: String?,
            @SerializedName("imdbID") val imdbID: String?,
            @SerializedName("Poster") val poster: String?
    )
}
