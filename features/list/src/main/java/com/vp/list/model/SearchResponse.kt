package com.vp.list.model

import com.google.gson.annotations.SerializedName

open class SearchResponse {

    companion object {
        private const val POSITIVE_RESPONSE = "True"
    }

    @SerializedName("Search")
    open val search: List<ListItem> = emptyList()

    @SerializedName("totalResults")
    val totalResults = 0

    @SerializedName("Response")
    private val response: String = ""

    fun hasResponse(): Boolean {
        return POSITIVE_RESPONSE == response
    }

}
