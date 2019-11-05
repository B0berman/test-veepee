package com.vp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.vp.list.model.ListItem
import com.vp.movies.data.remote.repository.MovieRepository

import java.util.ArrayList

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

import io.reactivex.rxkotlin.subscribeBy

class ListViewModel @Inject constructor(
        private val movieRepository: MovieRepository
) : ViewModel() {

    private val mMovies = MutableLiveData<SearchResult>()
    val movies: LiveData<SearchResult> = mMovies

    private val compositeDisposable = CompositeDisposable()

    private var currentTitle = ""
    private val aggregatedItems = ArrayList<ListItem>()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun refresh() {
        aggregatedItems.clear()
        searchMoviesByTitle(currentTitle, 1)
    }

    fun searchMoviesByTitle(title: String, page: Int) {
        if (page == 1 && title != currentTitle) {
            aggregatedItems.clear()
            currentTitle = title
            mMovies.value = SearchResult.inProgress()
        }
        movieRepository.search(title, page)
                .map { list -> list.map { ListItem(it) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            aggregatedItems.addAll(it)
                            mMovies.value = SearchResult.success(aggregatedItems, aggregatedItems.size)
                        },
                        onError = { mMovies.value = SearchResult.error() }
                )
                .addTo(compositeDisposable)
    }
}
