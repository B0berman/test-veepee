package com.vp.list.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
        @SerializedName("Response") val response: String,
        @SerializedName("totalResults") val totalResults: Int,
        @SerializedName("Search") val search: List<ListItem>? = emptyList()
)