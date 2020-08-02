package com.vp.list.viewmodel

import androidx.annotation.Keep
import com.vp.list.model.ListItem
import java.util.*

@Keep
data class SearchResult (val items: List<ListItem>, val totalResult: Int, val listState: ListState) {

    companion object {
        fun error() = SearchResult(emptyList(), 0, ListState.ERROR)

        fun success(items: List<ListItem>, totalResult: Int) =
                SearchResult(items, totalResult, ListState.LOADED)

        fun inProgress() =  SearchResult(emptyList(), 0, ListState.IN_PROGRESS)
        fun paging(): SearchResult? = SearchResult(emptyList(), 0, ListState.PAGING)
    }

}

