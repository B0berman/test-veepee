package com.vp.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.movie.abstraction.usecases.FavouriteMovieUseCase
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(private val detailService: DetailService,
                                           private val favouriteMovieUseCase: FavouriteMovieUseCase) : ViewModel() {

    companion object {
        val TAG = "DetailsViewModel"
    }

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val isFavourite: MutableLiveData<Boolean> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun isFavourite(): LiveData<Boolean> = isFavourite

    fun state(): LiveData<LoadingState> = loadingState

    fun fetchDetails() {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(DetailActivity.queryProvider.getMovieId()).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                details.postValue(response?.body())

                response?.body()?.title?.let {
                    title.postValue(it)
                }

                loadingState.value = LoadingState.LOADED
                checkFavouriteMovie()
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
    }

    fun favouriteButtonClicked() {
        isFavourite.value?.let {
            when (it) {
                false -> setMovieAsFavourite()
                true -> removeMovieFromFavourite()
            }
        }
    }

    fun checkFavouriteMovie() {
        favouriteMovieUseCase.getFavouriteMovieById(DetailActivity.queryProvider.getMovieId())
                .subscribeBy(
                        onSuccess = { isFavourite.postValue(true) },
                        onError = {
                            Log.d(TAG, it.localizedMessage)
                            isFavourite.postValue(false)
                        }
                )
    }

    private fun setMovieAsFavourite() {
        Log.d(TAG, "setMovieAsFavourite: ")
        details.value?.let { movie ->
            favouriteMovieUseCase.addMovieToFavourite(movie)
                    .subscribeBy(
                            onSuccess = {
                                Log.d(TAG, "Movie saved with id $it")
                                isFavourite.postValue(true)
                            },
                            onError = { Log.d(TAG, it.localizedMessage) }
                    )
        }
    }

    private fun removeMovieFromFavourite() {
        Log.d(TAG, "removeMovieFromFavourite: ")
        isFavourite.postValue(false)
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}