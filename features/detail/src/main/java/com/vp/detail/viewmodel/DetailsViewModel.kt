package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.datasource.DetailLocalDataSource
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
        private val detailService: DetailService,
        private val detailDataSource: DetailLocalDataSource
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    val favoriteState: LiveData<FavoriteStateState>

    init {
        val favoriteState = MediatorLiveData<FavoriteStateState>()
        val isFavorite = detailDataSource.isFavorite(DetailActivity.queryProvider.getMovieId())

        favoriteState.addSource(details) {
            favoriteState.postValue(
                    getFavoriteState(
                            isLoaded = it != null,
                            isPresent = isFavorite.value == true
                    )
            )
        }
        favoriteState.addSource(isFavorite) {
            favoriteState.postValue(
                    getFavoriteState(
                            isLoaded = details.value != null,
                            isPresent = it
                    )
            )
        }
        this.favoriteState = favoriteState
    }


    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState


    fun fetchDetails() {
        loadingState.value = LoadingState.IN_PROGRESS
        val imdbId = DetailActivity.queryProvider.getMovieId()
        val localMovie = detailDataSource.getDetail(imdbId)
        if (localMovie == null) {
            detailService.getMovie(DetailActivity.queryProvider.getMovieId())
                    .enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
                        override fun onResponse(
                                call: Call<MovieDetail>?,
                                response: Response<MovieDetail>?
                        ) {
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

        } else {
            details.postValue(localMovie)
            title.postValue(localMovie.title)
            loadingState.value = LoadingState.LOADED
        }
    }

    fun setAsFavorite() {
        detailDataSource.setAsFavorite(
                imdbID = DetailActivity.queryProvider.getMovieId(),
                movieDetail = requireNotNull(details.value)
        )
    }

    fun removeFavorite() {
        detailDataSource.removeAsFavorite(
                imdbID = DetailActivity.queryProvider.getMovieId()
        )
    }


    private fun getFavoriteState(isLoaded: Boolean, isPresent: Boolean): FavoriteStateState =
            when {
                !isLoaded -> FavoriteStateState.UNKNOWN
                isPresent -> FavoriteStateState.FAVORITE
                else -> FavoriteStateState.NOT_FAVORITE
            }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }

    enum class FavoriteStateState {
        UNKNOWN, FAVORITE, NOT_FAVORITE
    }
}
