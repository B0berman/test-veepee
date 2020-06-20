package com.vp.detail.viewmodel

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.data.local.FavoritesRepository
import com.vp.data.mapper.Mapper
import com.vp.data.model.MovieFavorite
import com.vp.detail.mapper.DetailToFavoriteMapper
import com.vp.detail.mapper.FavoriteToDetailMapper
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(private val detailService: DetailService,
                                           private val favoritesRepository: FavoritesRepository): ViewModel() {

    private val detailToFavoriteMapper: Mapper<MovieDetail, MovieFavorite> = DetailToFavoriteMapper()
    private val favoriteToDetailMapper: Mapper<MovieFavorite, MovieDetail> = FavoriteToDetailMapper()

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val isFavorite: MutableLiveData<Boolean> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun isFavorite(): LiveData<Boolean> = isFavorite

    init {
        isFavorite.value = false
    }

    fun fetchDetails(movieId: String) {
        loadingState.value = LoadingState.IN_PROGRESS
        Thread(Runnable{
            if (!fetchDetailsFromFavorites(movieId)) {
                fetchDetailsFromNetwork(movieId)
            }
        }).start()
    }

    @WorkerThread
    private fun fetchDetailsFromFavorites(movieId: String): Boolean {
        favoritesRepository.getFromId(movieId)?.let { favorite ->
            title.postValue(favorite.title)
            val detail = favoriteToDetailMapper.map(favorite)
            details.postValue(detail)
            isFavorite.postValue(true)
            loadingState.postValue(LoadingState.LOADED)
            return true
        }
        return false
    }

    private fun fetchDetailsFromNetwork(movieId: String) {
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

    fun favoriteMovie() {
        details.value?.let {
            Thread(Runnable{
                val favorite = detailToFavoriteMapper.map(it)
                favoritesRepository.insert(favorite)
                isFavorite.postValue(true)
            }).start()
        }
    }

    fun unfavoriteMovie() {
        details.value?.let {
            Thread(Runnable{
                favoritesRepository.deleteFromId(it.imdbID)
                isFavorite.postValue(false)
            }).start()
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}
