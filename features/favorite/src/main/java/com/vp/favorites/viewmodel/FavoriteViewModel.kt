package com.vp.favorites.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.CompositeDisposableCalls
import com.vp.detail.model.MovieDetail
import com.vp.exhaustive
import com.vp.favorite.usecase.GetFavorites
import com.vp.favorites.viewmodel.Action.Load
import com.vp.favorites.viewmodel.Action.TryAgain
import com.vp.favorites.viewmodel.ViewState.*
import javax.inject.Inject

internal sealed class ViewState {
    data class Content(val data: List<MovieDetail>) : ViewState()
    object Empty : ViewState()
    object Failure : ViewState()
    object Loading: ViewState()
}

internal sealed class Action {
    object Load : Action()
    object TryAgain : Action()
}

internal class FavoriteViewModel @Inject constructor(private val getFavorites: GetFavorites) : ViewModel() {
    private var disposable: CompositeDisposableCalls? = null

    val viewState = MutableLiveData<ViewState>()

    fun onAction(action: Action) {
        when (action) {
            is Load -> retrieveFavorites()
            is TryAgain -> retrieveFavorites()
        }.exhaustive
    }

    private fun retrieveFavorites() {
        disposable?.cancel()
        viewState.postValue(Loading)
        disposable = getFavorites.execute({ list ->
            if (list.isEmpty()) {
                viewState.postValue(Empty)
            } else {
                viewState.postValue(Content(list))
            }
        }, {
            viewState.postValue(Failure)
        })
    }

    override fun onCleared() {
        disposable?.cancel()
        super.onCleared()
    }
}
