package com.vp.favorites.viewmodel

import com.vp.favorites.model.FavoriteListItem
import java.util.Objects

class FavoriteSearchResult private constructor(
        val items: List<FavoriteListItem>,
        val totalResult: Int,
        val favoriteListState: FavoriteListState
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return (other as FavoriteSearchResult).let {
            totalResult == other.totalResult
                    && items == other.items
                    && favoriteListState === other.favoriteListState
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(items, totalResult, favoriteListState)
    }

    companion object {

        fun error(): FavoriteSearchResult {
            return FavoriteSearchResult(emptyList(), 0, FavoriteListState.ERROR)
        }

        fun success(items: List<FavoriteListItem>, totalResult: Int): FavoriteSearchResult {
            return FavoriteSearchResult(items, totalResult, FavoriteListState.LOADED)
        }

        fun inProgress(): FavoriteSearchResult {
            return FavoriteSearchResult(emptyList(), 0, FavoriteListState.IN_PROGRESS)
        }
    }
}
