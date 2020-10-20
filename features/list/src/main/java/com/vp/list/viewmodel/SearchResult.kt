package com.vp.list.viewmodel

import com.vp.list.model.ListItem
import com.vp.list.viewmodel.ListState.ERROR
import com.vp.list.viewmodel.ListState.IN_PROGRESS
import com.vp.list.viewmodel.ListState.LOADED
import com.vp.list.viewmodel.ListState.LOADING_MORE

data class SearchResult constructor(
    val items: List<ListItem>,
    val totalResult: Int,
    val listState: ListState
) {

    companion object {
        @JvmStatic fun error(): SearchResult {
            return SearchResult(emptyList(), 0, ERROR)
        }

        @JvmStatic fun success(items: List<ListItem>, totalResult: Int): SearchResult {
            return SearchResult(items, totalResult, LOADED)
        }

        @JvmStatic fun loadingMore(items: List<ListItem>): SearchResult {
            return SearchResult(items, 0, LOADING_MORE)
        }

        @JvmStatic fun inProgress(): SearchResult {
            return SearchResult(emptyList(), 0, IN_PROGRESS)
        }
    }
}
