package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private val mMovieId = MutableLiveData<String>()

    private val mFavorite = MediatorLiveData<Boolean>()
    val favorite: LiveData<Boolean> = mFavorite

    private val mMovie = MediatorLiveData<MovieDetail>()
    val movie: LiveData<MovieDetail> = mMovie

    private val mState = MutableLiveData<LoadingState>()
    val state: MutableLiveData<LoadingState> = mState

    init {
        mFavorite.addSource(mMovieId) { mMovieId.value?.let { id -> checkIfFavorite(id) } }
        mMovie.addSource(mMovieId) { mMovieId.value?.let { id -> fetchMovie(id) } }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun setMovieId(movieId: String) {
        mMovieId.value = movieId
    }

    fun toggleFavorite() {
        mMovie.value?.let {
            if (mFavorite.value == true) {
                removeFromFavorites(it.id)
            } else {
                addToFavorites(it)
            }
        }
    }

    private fun checkIfFavorite(movieId: String) {
        movieRepository.isFavorite(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = { mFavorite.value = false },
                        onSuccess = { mFavorite.value = true },
                        onError = { mFavorite.value = false }
                )
                .addTo(compositeDisposable)
    }

    private fun fetchMovie(movieId: String) {
        movieRepository.get(movieId)
                .map { MovieDetail(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state.value = LoadingState.IN_PROGRESS }
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
                        imdbID = movie.id,
                        title = movie.title,
                        year = movie.year,
                        runtime = movie.runtime,
                        director = movie.director,
                        plot = movie.plot,
                        poster = movie.poster
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onComplete = { mFavorite.value = true })
                .addTo(compositeDisposable)
    }

    private fun removeFromFavorites(imdbID: String) {
        movieRepository.removeFromFavorites(imdbID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onComplete = { mFavorite.value = false })
                .addTo(compositeDisposable)
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}