package com.vp.detail.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.favorite.FavoriteMovieRepository
import com.vp.favorite.model.Movie
import com.vp.favorite.toggleFavorite
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
    private val detailService: DetailService,
    private val favoriteRepository: FavoriteMovieRepository
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun isFavorite(): LiveData<Boolean> = details.map { it.isFavorite }

    fun state(): LiveData<LoadingState> = loadingState

    fun toggleFavorite() {
        viewModelScope.launch {
            val movie = details.value ?: error("Unexpected no value")
            favoriteRepository.toggleFavorite(movie.toMovie())
            details.postValue(movie.copy(isFavorite = favoriteRepository.isFavorite(movie.id)))
        }
    }

    fun fetchDetails(movieId: String) {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(movieId).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                viewModelScope.launch {
                    val movie = response?.body()
                    if (movie != null) {
                        val isFavorite = favoriteRepository.isFavorite(movie.id)
                        details.postValue(movie.copy(isFavorite = isFavorite))

                        movie.title.let(title::postValue)

                        loadingState.value = LoadingState.LOADED
                    }
                }
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

private fun MovieDetail.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        year = year,
        runtime = runtime,
        director = director,
        plot = plot,
        posterUri = Uri.parse(poster)
    )
}
