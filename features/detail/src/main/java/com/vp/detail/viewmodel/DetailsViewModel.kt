package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.favorites.model.FavoriteMovie
import com.vp.favorites.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
        private val favoriteRepository: FavoriteRepository,
        private val detailService: DetailService) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val isFavorite: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var movieId: String

    fun setMovieId(movieId: String) { this.movieId = movieId }

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun isFavorite(): LiveData<Boolean> = isFavorite

    fun fetchDetails() {
        loadingState.value = LoadingState.IN_PROGRESS
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
    }

    fun fetchFavorite() {
        isFavorite.value = favoriteRepository.isFavorite(movieId)
    }

    fun onFavoriteClick() {
        details.value?.let {
            val favoriteResult = favoriteRepository.onFavoriteSelection(it.toMovieDetail(movieId))
            isFavorite.value = favoriteResult
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}

private fun MovieDetail.toMovieDetail(movieId: String): FavoriteMovie {
    return FavoriteMovie(title = title, movieId = movieId, cover = poster)
}
