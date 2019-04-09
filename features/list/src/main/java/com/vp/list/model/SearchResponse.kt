package com.vp.list.model

import com.google.gson.annotations.SerializedName

class SearchResponse(@SerializedName("Response")
                     private var response: String) {

    @SerializedName("Search")
    var search: List<ListItem>? = emptyList()

    var totalResults: Int = 0

    fun hasResponse(): Boolean {
        return POSITIVE_RESPONSE == response
    }

    companion object {
        private const val POSITIVE_RESPONSE = "True"
    }
}