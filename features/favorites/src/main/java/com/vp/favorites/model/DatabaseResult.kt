package com.vp.favorites.model

import com.vp.list.model.ListItem

class DatabaseResult private constructor(val items: List<ListItem>, val listState: FavouriteListState) {

    companion object {
        fun empty() = DatabaseResult(emptyList(), FavouriteListState.EMPTY)

        fun fetched(items: List<ListItem>) = DatabaseResult(items, FavouriteListState.FETCHED)

        fun fetching() = DatabaseResult(emptyList(), FavouriteListState.FETCHING)
    }
}
