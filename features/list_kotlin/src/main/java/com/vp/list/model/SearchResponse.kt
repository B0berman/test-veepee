package com.vp.list.model

import com.google.gson.annotations.SerializedName
import com.vp.favorites.model.ListItem

class SearchResponse(@field:SerializedName("Response") private val response: String) {
    @SerializedName("Search")
    val search: List<ListItem>? = null
        get() = field ?: emptyList()
    val totalResults = 0

    fun hasResponse(): Boolean {
        return POSITIVE_RESPONSE == response
    }

    companion object {
        private const val POSITIVE_RESPONSE = "True"
    }
}