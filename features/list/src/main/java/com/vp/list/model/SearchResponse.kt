package com.vp.list.model

import com.google.gson.annotations.SerializedName

import java.util.Collections.emptyList

open class SearchResponse (
        @SerializedName("Search")
        val searches: List<Movie> = emptyList(),
        @SerializedName("totalResults")
        val totalResults: Int = 0,
        @SerializedName("Response")
        val response: String  = "",
        val hasResponse:Boolean = response == "True"
)
