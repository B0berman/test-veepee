package com.vp.list.model

import com.google.gson.annotations.SerializedName

class SearchResponse private
constructor(@SerializedName("Response") private val response: String) {

    @SerializedName("Search")
    val search: List<ListItem>? = null
        get() = field ?: emptyList()

    val totalResults = 0

    fun hasResponse(): Boolean = POSITIVE_RESPONSE == response

    companion object {
        private const val POSITIVE_RESPONSE = "True"
    }
}