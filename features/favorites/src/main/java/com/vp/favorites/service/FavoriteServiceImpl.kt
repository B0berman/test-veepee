package com.vp.favorites.service

import androidx.lifecycle.LiveData
import com.vp.favorites.model.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FavoriteServiceImpl(val favoriteDataSource: FavoriteDataSource) : CoroutineScope, FavoriteService {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun observeMovieItems(): LiveData<MovieItem> {
        return favoriteDataSource.observeMovieItem()
    }

    override fun observeMovieItemsList(): LiveData<List<MovieItem>> {
        return favoriteDataSource.observeMovieItemList()
    }

    override suspend fun refreshSelectedFavorite(imdbID: String) {
        updateFavoriteFromLocalDataSource(imdbID)
    }

    override suspend fun saveMovieItem(movieItem: MovieItem) {
        coroutineScope {
            launch { favoriteDataSource.saveMovieItem(movieItem) }
        }
    }

    override suspend fun deleteMovieItem(imdbID: String) {
        coroutineScope {
            launch { favoriteDataSource.deleteMovieItem(imdbID) }
        }
    }

    override suspend fun loadFavorites() {
        loadFavoriteFromLocalDataSource()
    }

    private suspend fun updateFavoriteFromLocalDataSource(selectedRate: String) {
        favoriteDataSource.refreshSelectedFavorite(selectedRate)
    }

    private suspend fun loadFavoriteFromLocalDataSource() {
        favoriteDataSource.loadFavorites()
    }
}