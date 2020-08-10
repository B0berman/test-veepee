package com.vp.list.model

import com.google.gson.annotations.SerializedName

open class SearchResponse private constructor(@field:SerializedName("Response") private val response: String) {
    @SerializedName("Search")
    val search: List<ListItem>? = null

    private val totalResults = 0

    open fun getTotalResult() = totalResults

    fun hasResponse() = POSITIVE_RESPONSE == response

    open fun getSearchList() = search?: emptyList()

    companion object {
        private const val POSITIVE_RESPONSE = "True"
    }
}