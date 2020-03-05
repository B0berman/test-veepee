package com.vp.api.model

import androidx.lifecycle.LiveData

interface FavoritesRepository {

    suspend fun addToFavorites(item: ListItem)

    suspend fun removeFromFavorites(id: String)

    fun getFavorites(): LiveData<List<ListItem>>

    suspend fun isMarkedAsFavorites(id: String): Boolean
}