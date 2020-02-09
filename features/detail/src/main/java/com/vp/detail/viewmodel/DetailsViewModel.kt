package com.vp.detail.viewmodel

import androidx.lifecycle.*
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.detail.service.FavoriteService
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
        private val detailService: DetailService,
        private val favoritesService: FavoriteService) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val favoriteState: MutableLiveData<FavoriteState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun favorite(): LiveData<FavoriteState> = favoriteState

    fun fetchDetails(movieId: String) {
        loadingState.value = LoadingState.IN_PROGRESS
        favoriteState.value = FavoriteState.IN_PROGRESS
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
                favoriteState.value = FavoriteState.FALSE
            }
        })
    }

    fun bindFavoriteObserver(lifecycleOwner: LifecycleOwner, movieId: String) {
        details.observe(lifecycleOwner, Observer {
            val isFavorite = favoritesService.isFavorite(movieId)
            favoriteState.value = if (isFavorite) FavoriteState.TRUE else FavoriteState.FALSE
        })
    }

    fun isFavorite() = favoriteState.value == FavoriteState.TRUE

    fun saveToFavorites(movieId: String) {
        details.value?.also {
            favoritesService.saveToFavorites(movieId, it)
        }
        favoriteState.value = FavoriteState.TRUE
    }

    fun removeFromFavorites(movieId: String) {
        details.value?.also {
            favoritesService.removeFromFavorites(movieId)
        }
        favoriteState.value = FavoriteState.FALSE
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }

    enum class FavoriteState {
        IN_PROGRESS, TRUE, FALSE
    }
}