package com.vp.list.model

import com.google.gson.annotations.SerializedName

import java.util.Collections.emptyList

class SearchResponse private constructor(
        @SerializedName("Response") private val response: String,
        @SerializedName("Search") val search: List<ListItem> = emptyList()
) {
    val totalResults: Int = 0

    val hasResponse
        get() = POSITIVE_RESPONSE == response

    companion object {
        private const val POSITIVE_RESPONSE = "True"
    }
}
