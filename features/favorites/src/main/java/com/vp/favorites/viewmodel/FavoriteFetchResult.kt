package com.vp.favorites.viewmodel

import com.vp.favorites.model.ListItem

class FavoriteFetchResult private constructor(
    val items: List<ListItem>,
    val listState: ListState
) {

    companion object {
        fun onError(): FavoriteFetchResult {
            return FavoriteFetchResult(emptyList(), ListState.ERROR)
        }

        fun onSuccess(
            items: List<ListItem>
        ): FavoriteFetchResult {
            return FavoriteFetchResult(items, ListState.LOADED)
        }

        fun inProgress(): FavoriteFetchResult {
            return FavoriteFetchResult(emptyList(), ListState.IN_PROGRESS)
        }
    }

}