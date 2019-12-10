package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.saneme87.roomdatabase.FavoriteDao
import com.gmail.saneme87.roomdatabase.model.FavoriteMovie
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(private val detailService: DetailService, private val favoriteDao: FavoriteDao) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val favState: MutableLiveData<Boolean> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun favState(): LiveData<Boolean> = favState

    fun details(): LiveData<MovieDetail> = details

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
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
    }

    fun fetchFavState() {
        viewModelScope.launch {
            val id = DetailActivity.queryProvider.getMovieId()
            favState.postValue(favoriteDao.getFavorite(id) != null)
        }
    }

    fun updateFavState(newState: Boolean) {
        viewModelScope.launch {
            val favorite = FavoriteMovie(DetailActivity.queryProvider.getMovieId(), details().value?.poster
                    ?: "")
            if (newState) {
                favoriteDao.insert(favorite)
            } else {
                favoriteDao.delete(favorite)
            }
            favState.postValue(newState)
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}