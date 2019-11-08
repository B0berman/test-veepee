package com.vp.list.viewmodel

import com.vp.list.model.ListItem

import java.util.Objects

class SearchResult private constructor(
        val items: List<ListItem>,
        val totalResult: Int,
        val listState: ListState
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return (other as SearchResult).let {
            totalResult == other.totalResult
                    && items == other.items
                    && listState === other.listState
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(items, totalResult, listState)
    }

    companion object {

        fun error(): SearchResult {
            return SearchResult(emptyList(), 0, ListState.ERROR)
        }

        fun success(items: List<ListItem>, totalResult: Int): SearchResult {
            return SearchResult(items, totalResult, ListState.LOADED)
        }

        fun inProgress(): SearchResult {
            return SearchResult(emptyList(), 0, ListState.IN_PROGRESS)
        }
    }
}
