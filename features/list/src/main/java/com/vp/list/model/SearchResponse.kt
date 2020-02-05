package com.vp.list.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("Response")
    val response: String,
    @SerializedName("Search")
    val search: MutableList<ListItem>?,
    val totalResults : Int = 0) {

    fun hasResponse(): Boolean {
        return POSITIVE_RESPONSE == response
    }

    companion object {
        private val POSITIVE_RESPONSE = "True"
    }

}