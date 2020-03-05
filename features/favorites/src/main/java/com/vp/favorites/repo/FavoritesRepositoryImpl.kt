package com.vp.favorites.repo

import androidx.lifecycle.LiveData
import com.vp.api.model.FavoritesRepository
import com.vp.api.model.ListItem
import com.vp.favorites.db.FavoritesDao
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val dao: FavoritesDao
) : FavoritesRepository {

    override suspend fun addToFavorites(item: ListItem) {
        dao.add(item)
    }

    override suspend fun removeFromFavorites(id: String) {
        dao.removeById(id)
    }

    override fun getFavorites(): LiveData<List<ListItem>> = dao.getAll()

    override suspend fun isMarkedAsFavorites(id: String): Boolean = dao.existsWithId(id)
}