package com.vp.list.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


private const val POSITIVE_RESPONSE = "True"

@Keep
data class SearchResponse(
        @SerializedName("Response")  val response: String,
        @SerializedName("Search") val search: List<ListItem> = emptyList(),
        val totalResults : Int = 0
){
    val hasResponse =  POSITIVE_RESPONSE == response
}

