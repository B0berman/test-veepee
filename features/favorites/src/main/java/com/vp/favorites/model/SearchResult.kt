package com.vp.favorites.model

class SearchResult private constructor(val items: List<FavoriteItem>, val listState: ListState) {

    companion object {
        fun error(): SearchResult {
            return SearchResult(emptyList(), ListState.ERROR)
        }

        fun success(items: List<FavoriteItem>): SearchResult {
            return SearchResult(items, ListState.LOADED)
        }

        fun inProgress(): SearchResult {
            return SearchResult(emptyList(), ListState.IN_PROGRESS)
        }

        fun noData(): SearchResult {
            return SearchResult(emptyList(), ListState.NO_DATA)
        }
    }
}

enum class ListState {
    IN_PROGRESS, LOADED, ERROR, NO_DATA
}