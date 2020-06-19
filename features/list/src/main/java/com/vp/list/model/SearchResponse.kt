package com.vp.list.model

import com.google.gson.annotations.SerializedName

open class SearchResponse private constructor(@SerializedName("Response") private val response: String) {
    @SerializedName("Search")
    val search: List<ListItem>? = null
        get() = field ?: emptyList()

    @SerializedName("totalResults")
    val totalResults = 0

    fun hasResponse(): Boolean {
        return POSITIVE_RESPONSE == response
    }

    companion object {
        private const val POSITIVE_RESPONSE = "True"
    }
}
