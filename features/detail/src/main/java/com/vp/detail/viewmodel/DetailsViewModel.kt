package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.persistence.model.FavoriteData
import com.vp.persistence.storage.AppPreferences
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
    private val detailService: DetailService,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val isFavorite: MutableLiveData<Boolean> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun isFavorite(): LiveData<Boolean> = isFavorite

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun fetchDetails() {
        loadingState.value = LoadingState.IN_PROGRESS
        val movieId = DetailActivity.queryProvider.getMovieId()
        detailService.getMovie(movieId).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                details.postValue(response?.body())

                response?.body()?.title?.let {
                    title.postValue(it)

                }

                loadingState.value = LoadingState.LOADED
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
        val bool = appPreferences.isMovieFavorite(movieId)
        isFavorite.postValue(bool)
    }

    fun setMovieFavorite() {
        val movieId = DetailActivity.queryProvider.getMovieId()
        appPreferences.setMovieFavorite(
            FavoriteData(
                id = movieId,
                title = details.value?.title ?: "",
                year =  details.value?.year ?: ""
            )
        )
        isFavorite.postValue(true)
    }

    fun removeMovieFavorite(){
        val movieId = DetailActivity.queryProvider.getMovieId()
        appPreferences.removeMovieFavorite(movieId)
        isFavorite.postValue(false)
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}