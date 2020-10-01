package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.model.FavoriteItem
import com.vp.favorites.model.SearchResult
import com.vp.favorites.service.FavoritesService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(private val favoritesService: FavoritesService) : ViewModel() {

    private val _favorites: MutableLiveData<SearchResult> = MutableLiveData()
    val favorites: LiveData<SearchResult>
        get() = _favorites

    fun fetchFavorites(favoritesIds: Set<String>?) {

        _favorites.value = SearchResult.inProgress()

        if (favoritesIds == null || favoritesIds.isEmpty()) {
            _favorites.value = SearchResult.noData()
        } else {

            val observables = mutableListOf<Observable<FavoriteItem>>()

            favoritesIds.forEach {
                observables.add(favoritesService.getMovie(it))
            }

            Observable.zip(observables) {
                it.toList()
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                val favoriteItems = it.filterIsInstance<FavoriteItem>()
                                _favorites.value = SearchResult.success(favoriteItems)
                            },
                            {
                                _favorites.value = SearchResult.error()
                            }
                    )
        }
    }
}