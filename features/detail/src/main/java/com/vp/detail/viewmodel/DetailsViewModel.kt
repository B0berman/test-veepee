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

    fun fetchDetails() {
        loadingState.value = LoadingState.IN_PROGRESS
        favoriteState.value = FavoriteState.IN_PROGRESS
        detailService.getMovie(DetailActivity.queryProvider.getMovieId()).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
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

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        details.observe(lifecycleOwner, Observer {
            val isFavorite = favoritesService.isFavorite(DetailActivity.queryProvider.getMovieId())
            favoriteState.value = if (isFavorite) FavoriteState.TRUE else FavoriteState.FALSE
        })
    }

    fun isFavorite() = favoriteState.value == FavoriteState.TRUE

    fun saveToFavorites() {
        details.value?.also {
            favoritesService.saveToFavorites(DetailActivity.queryProvider.getMovieId(), it)
        }
        favoriteState.value = FavoriteState.TRUE
    }

    fun removeFromFavorites() {
        details.value?.also {
            favoritesService.removeFromFavorites(DetailActivity.queryProvider.getMovieId())
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