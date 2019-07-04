package com.vp.detail.viewmodel

import androidx.lifecycle.*
import com.vp.core.favorites.FavoriteMoviesRepository
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class DetailsViewModel(
        private val movieId: String,
        private val detailService: DetailService,
        private val favoriteRepository: FavoriteMoviesRepository
) : ViewModel() {

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun isFavorite(): LiveData<Boolean> = favoriteRepository.isFavorite(movieId)

    fun switchFavorite() {
        scope.launch {
            favoriteRepository.setFavorite(
                    movie = details.awaitNonNull().toFavoriteMovie(),
                    favorite = isFavorite().awaitNonNull().not()
            )
        }
    }

    fun fetchDetails() {
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

    override fun onCleared() {
        scope.cancel()
        super.onCleared()
    }

    private fun MovieDetail.toFavoriteMovie() = FavoriteMoviesRepository.Movie(
            id = movieId,
            title = title,
            year = year,
            director = director,
            poster = poster
    )

    data class Dependencies(
            val detailService: DetailService,
            val favoriteRepository: FavoriteMoviesRepository
    )

    class Factory(
            private val movieId: String,
            private val dependencies: Dependencies
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = DetailsViewModel(
                movieId, dependencies.detailService, dependencies.favoriteRepository
        ) as T
    }
}