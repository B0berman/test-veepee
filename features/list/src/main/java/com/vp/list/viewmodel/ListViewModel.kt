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

    private val compositeDisposable = CompositeDisposable()

    private val mMovies = MutableLiveData<SearchResult>()
    val movies: LiveData<SearchResult> = mMovies

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            val results = it.search?.map { item -> ListItem(item) } ?: emptyList()
                            aggregatedItems.addAll(results)
                            mMovies.value = SearchResult.success(aggregatedItems, it.totalResults)
                        },
                        onError = { mMovies.value = SearchResult.error() }
                )
                .addTo(compositeDisposable)
    }
}
