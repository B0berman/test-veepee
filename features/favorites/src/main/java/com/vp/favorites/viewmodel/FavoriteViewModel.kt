package com.vp.favorites.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.interactor.GetMyFavoriteMoviesUseCase
import com.vp.favorites.interactor.UseCase
import com.vp.favorites.model.BasicMovie
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val getMyFavoriteMoviesUseCase: GetMyFavoriteMoviesUseCase
) : ViewModel() {

    private val movies: MutableLiveData<List<BasicMovie>> = MutableLiveData()
    private val error: MutableLiveData<Throwable> = MutableLiveData()

    fun movies(): LiveData<List<BasicMovie>> = movies
    fun error(): LiveData<Throwable> = error

    fun getMyFavoriteMovies() {
        getMyFavoriteMoviesUseCase.callback = object : UseCase.Callback<List<BasicMovie>> {
            override fun onSuccess(result: List<BasicMovie>) {
                movies.postValue(result)
            }

            override fun onComplete() {}

            override fun onError(result: Throwable) {
                Log.e(TAG, "onError: ", result)
                error.postValue(result)
            }
        }

        getMyFavoriteMoviesUseCase.execute(UseCase.EmptyParams)
    }


    override fun onCleared() {
        getMyFavoriteMoviesUseCase.cancel()
        super.onCleared()
    }

    companion object {
        private val TAG = FavoriteViewModel::class.java.simpleName
    }
}