package com.vp.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.model.mapper.MovieMapper
import com.vp.detail.service.DetailService
import com.vp.favorites.interactor.DeleteMovieAsFavoriteUseCase
import com.vp.favorites.interactor.IsMovieFavoriteUseCase
import com.vp.favorites.interactor.StoreMovieAsFavoriteUseCase
import com.vp.favorites.interactor.UseCase
import com.vp.favorites.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
    private val detailService: DetailService,
    private val isMovieFavoriteUseCase: IsMovieFavoriteUseCase,
    private val storeMovieAsFavoriteUseCase: StoreMovieAsFavoriteUseCase,
    private val deleteMovieAsFavoriteUseCase: DeleteMovieAsFavoriteUseCase
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val isMovieFavorite: MutableLiveData<Boolean> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun isFavorite(): LiveData<Boolean> = isMovieFavorite

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun fetchDetails() {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(DetailActivity.queryProvider.getMovieId())
            .enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
                override fun onResponse(
                    call: Call<MovieDetail>?,
                    response: Response<MovieDetail>?
                ) {
                    checkIfMovieIsStoredAsFavorite(DetailActivity.queryProvider.getMovieId())
                    with(response?.body()) {
                        details.postValue(this)
                        title.postValue(this?.title ?: "")
                    }

                    loadingState.value = LoadingState.LOADED
                }

                override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                    details.postValue(null)
                    loadingState.value = LoadingState.ERROR
                }
            })
    }

    fun storeMovie() {
        with(DetailActivity.queryProvider.getMovieId()) {
            checkIfMovieIsStoredAsFavorite(this) { isFavorite ->
                when (isFavorite) {
                    true -> deleteMovieAsFavorite(this)
                    false -> storeMovieAsFavorite(this)
                }
            }
        }
    }

    private fun checkIfMovieIsStoredAsFavorite(id: String, nextStep: (Boolean) -> Unit = {}) {
        isMovieFavoriteUseCase.callback = object : UseCase.Callback<Boolean> {
            override fun onComplete() {}

            override fun onSuccess(result: Boolean) {
                isMovieFavorite.postValue(result)
                nextStep(result)
            }

            override fun onError(result: Throwable) {
                Log.e(TAG, result.message, result)
            }

        }
        isMovieFavoriteUseCase.execute(id)
    }

    private fun storeMovieAsFavorite(id: String) {
        storeMovieAsFavoriteUseCase.callback = object : UseCase.Callback<Nothing> {
            override fun onSuccess(result: Nothing) {}

            override fun onComplete() {
                isMovieFavorite.postValue(true)
            }

            override fun onError(result: Throwable) {
                Log.e(TAG, result.message, result)
            }
        }

        details.value?.let {
            storeMovieAsFavoriteUseCase.execute(MovieMapper.transformFromDetail(id, it))
        }
    }

    private fun deleteMovieAsFavorite(id: String) {
        deleteMovieAsFavoriteUseCase.callback = object : UseCase.Callback<Nothing> {
            override fun onSuccess(result: Nothing) {}

            override fun onComplete() {
                isMovieFavorite.postValue(false)
            }

            override fun onError(result: Throwable) {
                Log.e(TAG, result.message, result)
            }
        }

        deleteMovieAsFavoriteUseCase.execute(id)
    }

    override fun onCleared() {
        isMovieFavoriteUseCase.cancel()
        storeMovieAsFavoriteUseCase.cancel()
        deleteMovieAsFavoriteUseCase.cancel()
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }

    companion object {
        private val TAG = DetailsViewModel::class.java.simpleName
    }
}