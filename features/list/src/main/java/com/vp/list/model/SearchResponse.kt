package com.vp.list.model

import com.google.gson.annotations.SerializedName

class SearchResponse private constructor(
    @SerializedName("Response")
    val response: String,
    @SerializedName("Search")
    val search: MutableList<ListItem>,
    val totalResults: Int
) {

    companion object {
        private val POSITIVE_RESPONSE: String = "True"
    }

    fun hasResponse(): Boolean {
        return POSITIVE_RESPONSE == response
    }
}