package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.exhaustive
import com.vp.favorite.service.FavDisposable
import com.vp.favorite.service.FavoritesService
import javax.inject.Inject

internal sealed class ViewState {
    data class IsFavorite(val isFavorite: Boolean) : ViewState()
}

internal sealed class Action {
    data class Favorite(val id: String) : Action()
    data class RemoveFavorite(val id: String) : Action()
}

internal class FavoriteViewModel @Inject constructor(
        private val favoritesService: FavoritesService
) : ViewModel() {

    private var favDisposable: FavDisposable? = null
    private val favorite: MutableLiveData<ViewState> = MutableLiveData()


    fun viewState(movieId: String): LiveData<ViewState> {
        favDisposable?.invoke()
        favDisposable = favoritesService.listenId(movieId) { isFavorite ->
            favorite.value = ViewState.IsFavorite(isFavorite)
        }
        return favorite
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.Favorite -> setAsFavorite(action.id)
            is Action.RemoveFavorite -> removeFavorite(action.id)
        }.exhaustive
    }


    override fun onCleared() {
        favDisposable?.invoke()
        super.onCleared()
    }

    private fun setAsFavorite(movieId: String) {
        favoritesService.save(movieId, true)
    }

    private fun removeFavorite(movieId: String) {
        favoritesService.save(movieId, false)
    }
}