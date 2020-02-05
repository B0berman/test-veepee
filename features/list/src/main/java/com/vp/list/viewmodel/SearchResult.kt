package com.vp.list.viewmodel

import com.vp.list.model.ListItem
import java.util.*

class SearchResult private constructor(
    val items: List<ListItem>,
    val totalResult: Int,
    val listState: ListState
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchResult

        if (items != other.items) return false
        if (totalResult != other.totalResult) return false
        if (listState != other.listState) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(items, totalResult, listState)
    }

    companion object {
        fun error() = SearchResult(emptyList(), 0, ListState.ERROR)

        fun success(items: List<ListItem>, totalResult: Int) = SearchResult(items, totalResult, ListState.LOADED)

        fun inProgress() = SearchResult(emptyList(), 0, ListState.IN_PROGRESS)

        fun loadingMore() = SearchResult(emptyList(), 0, ListState.LOADING_MORE)
    }
}