package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.favorites.database.FavoriteMovieDatabase
import com.vp.favorites.model.FavoriteMovie
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
        private val detailService: DetailService,
        private val favoriteMovieDatabase: FavoriteMovieDatabase
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val isFavorite: MutableLiveData<Boolean> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun isFavorite(): LiveData<Boolean> = isFavorite

    fun fetchDetails(movieId: String) {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(movieId).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>) {
                details.postValue(response.body())

                response.body()?.title?.let {
                    title.postValue(it)
                }

                loadingState.value = LoadingState.LOADED

                response.body()?.id?.let {
                    isFavorite.postValue(favoriteMovieDatabase.contains(it))
                }
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
    }

    fun toggleFavorite() {
        val movieDetail = details.value ?: return
        if (favoriteMovieDatabase.contains(movieDetail.id)) {
            // Remove
            favoriteMovieDatabase.remove(movieDetail.id)
            isFavorite.postValue(false)
        } else {
            // Add
            val favoriteMovie = FavoriteMovie(
                    id = movieDetail.id,
                    title = movieDetail.title,
                    year = movieDetail.year
            )
            favoriteMovieDatabase.add(favoriteMovie)
            isFavorite.postValue(true)
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}