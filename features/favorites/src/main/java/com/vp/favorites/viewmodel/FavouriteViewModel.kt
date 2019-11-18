package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.model.FavouriteItem
import com.vp.movie.abstraction.usecases.FavouriteMovieUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class FavouriteViewModel @Inject constructor(private val favouriteMovieUseCase: FavouriteMovieUseCase) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val favouriteMovieList = MutableLiveData<List<FavouriteItem>>()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun favouriteMovies(): LiveData<List<FavouriteItem>> = favouriteMovieList
    fun state(): LiveData<LoadingState> = loadingState

    fun loadFavouriteMovies() {
        loadingState.value = LoadingState.IN_PROGRESS
        compositeDisposable.add(favouriteMovieUseCase.getFavouriteMovies()
                .map { list -> list.map { item -> FavouriteItem(item.title, item.year, item.imdbID, item.poster) } }
                .subscribeBy(
                        onNext = {
                            loadingState.postValue(LoadingState.LOADED)
                            favouriteMovieList.postValue(it)
                        },
                        onError = { loadingState.postValue(LoadingState.ERROR) },
                        onComplete = {}
                ))

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}