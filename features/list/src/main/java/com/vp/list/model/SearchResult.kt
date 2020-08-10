package com.vp.list.model

class SearchResult private constructor(val items: List<ListItem>, private val hasResponse: Boolean, val totalResult: Int) {

    fun hasResponse() = hasResponse

    companion object {
        fun error() = SearchResult(emptyList(), false, 0)

        fun success(items: List<ListItem>, totalResult: Int) = SearchResult(items, true, totalResult)
    }
}