package com.vp.favorites.viewmodel

import com.vp.favorites.model.ListItem
import java.util.*

class FavoriteResult(val items: List<ListItem>? = null, val totalResult: Int = 0, val favoriteState: FavoriteState? = null) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that: FavoriteResult = o as FavoriteResult
        return totalResult == that.totalResult &&
                items == that.items && favoriteState == that.favoriteState
    }

    fun error(): FavoriteResult? {
        return FavoriteResult(emptyList(), 0, FavoriteState.ERROR)
    }

    fun success(items: List<ListItem>, totalResult: Int): FavoriteResult? {
        return FavoriteResult(items, totalResult, FavoriteState.LOADED)
    }

    fun inProgress(): FavoriteResult? {
        return FavoriteResult(emptyList(), 0, FavoriteState.IN_PROGRESS)
    }
    override fun hashCode(): Int {
        return Objects.hash(items, totalResult, favoriteState)
    }
}