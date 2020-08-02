package com.vp.list.model

data class SearchResult (
        val items: List<ListItem>,
        val hasResponse: Boolean,
        val totalResult: Int
) {

    companion object {
        fun error() =  SearchResult(emptyList(), false, 0)

        fun success(items: List<ListItem>, totalResult: Int) =
                SearchResult(items, true, totalResult)
    }

}