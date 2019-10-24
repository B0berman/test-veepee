package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.model.MovieDetail
import com.vp.detail.model.toMovieDBEntity
import com.vp.detail.service.DetailService
import com.vp.storage.MoviesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback
import kotlin.coroutines.CoroutineContext

class DetailsViewModel @Inject constructor(
        private val detailService: DetailService,
        private val movieDao: MoviesDao
) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun fetchDetails(imdbID: String) {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(imdbID).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
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

    fun favoriteClicked() {
        details.value?.let {movieDetail ->
            launch {
                movieDao.insertMovie(movieDetail.toMovieDBEntity())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}