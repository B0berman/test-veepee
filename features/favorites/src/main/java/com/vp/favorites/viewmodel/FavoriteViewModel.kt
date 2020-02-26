package com.vp.favorites.viewmodel

import androidx.lifecycle.*
import com.vp.favorites.model.ListItem
import com.vp.favorites.model.MovieItem
import com.vp.favorites.service.FavoriteService
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(favoriteService: FavoriteService) : ViewModel() {
    private val liveData: MutableLiveData<FavoriteResult> = MutableLiveData()


    val startUpdate: MutableLiveData<Boolean> = MutableLiveData()

    init {
        startUpdate.value = false
    }

    val _movieItem: LiveData<FavoriteResult> = startUpdate.switchMap { update ->
        if (update) {
            viewModelScope.launch {
                favoriteService.loadFavorites()
            }
        }
        favoriteService.observeMovieItemsList().distinctUntilChanged().switchMap { filterFavorite(it) }
    }

    val movieItem: LiveData<FavoriteResult> = Transformations.map(_movieItem) {
        _movieItem.value
    }

    private fun filterFavorite(list: List<MovieItem>): LiveData<FavoriteResult> {
        val items = mutableListOf<ListItem>()
        for( item in list ){
            val i = ListItem(item.title, item.year, item.imdbID, item.poster)
            items.add(i)
        }
        val result = FavoriteResult(items, items.size, FavoriteState.LOADED)
        return MutableLiveData<FavoriteResult>(result)
    }

    internal fun loadFavoriteMoviesByTitle(page: Int) {
        startUpdate.value = true
    }
}