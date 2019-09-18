package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.mapper.MovieToFavoriteMovieMapper
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.persistance.BACKGROUND
import com.vp.persistance.RepositoryDB
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
    private val detailService: DetailService,
    private val movieDetaToFavoriteMovieMapper: MovieToFavoriteMovieMapper,
    private val repositoryDB: RepositoryDB
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    private val _isFavoriteLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isFavoriteLiveData: LiveData<Boolean> = _isFavoriteLiveData

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

    fun fetchIsFavorite(movieId: String) {
        BACKGROUND.submit {
            val isFavorite = repositoryDB.findById(movieId)?.let {
                true } ?: false

            _isFavoriteLiveData.postValue(isFavorite)
        }
    }

    fun toogleFavorite(movieId: String) {
        BACKGROUND.submit {
            val favMovie = movieDetaToFavoriteMovieMapper
                .map(movieId, details().value!!)
            repositoryDB.toogleFavorite(favMovie)
            val isFavorite = _isFavoriteLiveData.value
            _isFavoriteLiveData.postValue(!isFavorite!!)
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}