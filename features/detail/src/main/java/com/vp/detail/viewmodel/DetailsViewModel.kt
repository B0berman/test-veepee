package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.movies.data.model.MovieEntity
import com.vp.movies.data.remote.repository.MovieRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
        private val movieRepository: MovieRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val mFavorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean> = mFavorite

    private val mMovie = MutableLiveData<MovieDetail>()
    val movie: LiveData<MovieDetail> = mMovie

    private val mState = MutableLiveData<LoadingState>()
    val state: MutableLiveData<LoadingState> = mState

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun toogleFavorite() {
        mMovie.value?.let {
            if (mFavorite.value == true) {
                removeFromFavorites(DetailActivity.queryProvider.getMovieId())
            } else {
                addToFavorites(it)
            }
        }
    }

    fun removeFromFavorites() {
        mMovie.value?.let { removeFromFavorites(DetailActivity.queryProvider.getMovieId()) }
    }

    fun checkIfFavorites() {
        movieRepository.isFavorite(DetailActivity.queryProvider.getMovieId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = { mFavorite.value = false },
                        onSuccess = { mFavorite.value = true },
                        onError = { mFavorite.value = false }
                )
                .addTo(compositeDisposable)
    }

    fun fetchDetails() {
        state.value = LoadingState.IN_PROGRESS
        movieRepository.get(DetailActivity.queryProvider.getMovieId())
                .map { MovieDetail(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            mMovie.value = it
                            state.value = LoadingState.LOADED
                        },
                        onError = {
                            mMovie.value = null
                            state.value = LoadingState.ERROR
                        }
                )
                .addTo(compositeDisposable)
    }

    private fun addToFavorites(movie: MovieDetail) {
        movieRepository.addToFavorites(
                MovieEntity(
                        imdbID = DetailActivity.queryProvider.getMovieId(),
                        title = movie.title,
                        year = movie.year,
                        runtime = movie.runtime,
                        director = movie.director,
                        plot = movie.plot,
                        poster = movie.poster
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = { mFavorite.value = true }
                )
                .addTo(compositeDisposable)
    }

    private fun removeFromFavorites(imdbID: String) {
        movieRepository.removeFromFavorites(imdbID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = { mFavorite.value = false }
                )
                .addTo(compositeDisposable)
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}