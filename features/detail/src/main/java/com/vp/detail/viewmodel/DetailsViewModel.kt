package com.vp.detail.viewmodel

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.localdatasource.DetailsLocalDataSource
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback


class DetailsViewModel @Inject constructor(
        private val detailService: DetailService,
        private val detailsLocalDataSource: DetailsLocalDataSource
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun toggleFavorite(){
        val provisionalDetailsValueOrNull = details.value?.copy(favorite = !details.value!!.favorite)
        provisionalDetailsValueOrNull?.let {movie ->
            // I use a AsyncTask just because coroutines are experimental in this kotlin version and I don´t know if
            // libraries are allowed like the reactive ones, I now other methods like threading
            // but I think async task is better when no observables or suspend functions are allowed
            AsyncTask.execute {
                if(detailsLocalDataSource.updateMovie(movie)){ details.postValue(movie) }
            }
        }
    }

    fun fetchDetails(movieId: String) {
        loadingState.value = LoadingState.IN_PROGRESS
        // I use a AsyncTask just because coroutines are experimental in this kotlin version and I don´t know if
        // libraries are allowed like the reactive ones, I now other methods like threading
        // but I think async task is better when no observables or suspend functions are allowed
        AsyncTask.execute {
            detailsLocalDataSource.getMovieById(movieId)?.let { movie ->
                try {
                    details.postValue(movie)
                    title.postValue(movie.title)
                    loadingState.postValue(LoadingState.LOADED)
                }catch (e: Exception) {
                    fetchDetails(movieId)
                }
            } ?: fetchDetailsFromApi(movieId)
        }
    }

    private fun fetchDetailsFromApi(movieId : String) {
        detailService.getMovie(movieId).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                val movieOrNull = response?.body()
                details.postValue(movieOrNull)

                movieOrNull?.let { movie ->
                    title.postValue(movie.title)
                    // I use a AsyncTask just because coroutines are experimental and I don´t know if
                    // libraries are allowed like the reactive ones, I now other methods like threading
                    // but I think async task is better when no observables or suspend functions are allowed
                    AsyncTask.execute {
                        detailsLocalDataSource.insertMovie(movie)
                    }
                }

                loadingState.postValue(LoadingState.LOADED)
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }


}