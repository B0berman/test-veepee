package com.vp.list.model

import com.vp.favorites.model.ListItem

class SearchResult private constructor(val items: List<ListItem>, private val hasResponse: Boolean, val totalResult: Int) {
    fun hasResponse(): Boolean {
        return hasResponse
    }

    companion object {
        fun error(): SearchResult {
            return SearchResult(emptyList(), false, 0)
        }

        fun success(items: List<ListItem>, totalResult: Int): SearchResult {
            return SearchResult(items, true, totalResult)
        }

        fun inProgress(): SearchResult? {
            return SearchResult(emptyList(), false, 0)
        }
    }
}
