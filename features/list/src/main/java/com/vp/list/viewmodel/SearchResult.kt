package com.vp.list.viewmodel

import com.vp.list.model.ListItem
import java.util.*

class SearchResult private constructor(val items: List<ListItem>, val totalResult: Int, val listState: ListState) {

    companion object {

        fun error(): SearchResult = SearchResult(emptyList(), 0, ListState.ERROR)

        fun success(items: List<ListItem>, totalResult: Int): SearchResult = SearchResult(items, totalResult, ListState.LOADED)

        fun inProgress(): SearchResult = SearchResult(emptyList(), 0, ListState.IN_PROGRESS)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as SearchResult?
        return totalResult == that!!.totalResult &&
                items == that.items &&
                listState === that.listState
    }

    override fun hashCode(): Int {
        return Objects.hash(items, totalResult, listState)
    }
}