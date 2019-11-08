package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.model.FavoriteListItem

import com.vp.movies.data.remote.repository.MovieRepository

import java.util.ArrayList

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class FavoriteListViewModel @Inject constructor(
        private val movieRepository: MovieRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val mMovies = MutableLiveData<FavoriteSearchResult>()
    val movies: LiveData<FavoriteSearchResult> = mMovies

    init {
        retrieveFavorites()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun refresh() {
        retrieveFavorites()
    }

    private fun retrieveFavorites() {
        movieRepository.getFavorites()
                .map { list -> list.map { FavoriteListItem(it) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mMovies.value = FavoriteSearchResult.inProgress() }
                .subscribeBy(
                        onNext = { mMovies.value = FavoriteSearchResult.success(it, it.size) },
                        onError = { mMovies.value = FavoriteSearchResult.error() }
                )
                .addTo(compositeDisposable)
    }
}
