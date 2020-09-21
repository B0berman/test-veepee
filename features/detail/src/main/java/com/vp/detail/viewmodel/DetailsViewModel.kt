package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.model.FavoriteMovie
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.favorites.database.FavoritesMoviesDb
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

private const val TAG = "DetailsViewModel"

class DetailsViewModel @Inject constructor(private val detailService: DetailService) : ViewModel() {


    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    private val isFavorite: MutableLiveData<Boolean> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun isFavorite(): LiveData<Boolean> = isFavorite

    fun isMovieFavorite(movieId: String, favoritesMoviesDb: FavoritesMoviesDb) {
        if (favoritesMoviesDb.favoriteMoviesDao().getFavoriteMovieById(movieId) != null) {
            isFavorite.postValue(true)
        } else {
            isFavorite.postValue(false)
        }
    }

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

    fun updateMovie(movieId: String, movie: MovieDetail?, favoritesMoviesDb: FavoritesMoviesDb) {
        if (favoritesMoviesDb.favoriteMoviesDao().getFavoriteMovieById(movieId) == null) {
            movie?.let { movieDetailToFavoriteMovie(movieId, it) }?.let { favoritesMoviesDb.favoriteMoviesDao().insertFavoriteMovie(it) }
            isFavorite.value = true
        } else {
            movie?.let { movieDetailToFavoriteMovie(movieId, it) }?.let { favoritesMoviesDb.favoriteMoviesDao().deleteFavoriteMovie(it) }
            isFavorite.value = false
        }
    }

    fun movieDetailToFavoriteMovie(imdbId: String, movie: MovieDetail): FavoriteMovie {
        return FavoriteMovie(imdbId, movie.title, movie.year, movie.runtime,
                movie.director, movie.plot, movie.poster)
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}