package com.vp.list.model

import com.google.gson.annotations.SerializedName

import java.util.Collections.emptyList

val POSITIVE_RESPONSE = "True"

data class SearchResponse(
        @SerializedName("Response")
        val response: String,
        @SerializedName("Search")
        val search: List<ListItem>?,
        @SerializedName("totalResults")
        val totalResults: Int
)

fun SearchResponse.hasResponse(): Boolean {
    return POSITIVE_RESPONSE == response
}
