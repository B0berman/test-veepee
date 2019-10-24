package com.vp.list.model

import com.google.gson.annotations.SerializedName

import java.util.Collections.emptyList

data class SearchResponse (
        @SerializedName("Search")
        val search: List<ListItem> = emptyList(),
        @SerializedName("totalResults")
        val totalResults: Int = 0,
        @SerializedName("Response")
        val response: String  = "",
        val hasResponse:Boolean = response == "True"
)
