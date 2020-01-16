package com.vp.list.model

import com.google.gson.annotations.SerializedName

class SearchResponse constructor(
    @SerializedName("Search")
    val search: List<ListItem>,
    val totalResults: Int
)