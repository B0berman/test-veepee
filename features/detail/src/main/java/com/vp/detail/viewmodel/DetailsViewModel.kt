package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.api.model.FavoritesRepository
import com.vp.api.model.ListItem
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
    private val detailService: DetailService,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun fetchDetails(id: String) {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(id).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                details.postValue(response?.body())
                loadingState.value = LoadingState.LOADED
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
    }

    fun onFavouriteChanged(id: String) {
        viewModelScope.launch {
            if (favoritesRepository.isMarkedAsFavorites(id)) {
                favoritesRepository.removeFromFavorites(id)
            } else {
                details.value?.apply {
                    favoritesRepository.addToFavorites(ListItem(id, title, year, poster))
                }
            }
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}