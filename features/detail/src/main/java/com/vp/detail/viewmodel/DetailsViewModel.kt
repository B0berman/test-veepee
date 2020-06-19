package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.data.local.FavoritesRepository
import com.vp.data.mapper.Mapper
import com.vp.data.model.MovieFavorite
import com.vp.detail.mapper.DetailToFavoriteMapper
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(private val detailService: DetailService,
                                           private val favoritesRepository: FavoritesRepository): ViewModel() {

    private val detailToFavoriteMapper: Mapper<MovieDetail, MovieFavorite> = DetailToFavoriteMapper()

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun fetchDetails(movieId: String) {
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

    fun favoriteMovie(): Boolean {
        details.value?.let {
            val favorite = detailToFavoriteMapper.map(it)
            Thread(Runnable{
                favoritesRepository.insert(favorite)
            }).start()
            return true
        }
        return false
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}
