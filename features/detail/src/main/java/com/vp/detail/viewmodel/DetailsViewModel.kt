package com.vp.detail.viewmodel

import android.text.TextUtils
import androidx.lifecycle.*
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.favorites.model.MovieItem
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback
import com.vp.favorites.service.FavoriteService
import kotlinx.coroutines.launch

class DetailsViewModel @Inject constructor(private val detailService: DetailService, internal val favoriteService: FavoriteService) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val startUpdate: MutableLiveData<Boolean> = MutableLiveData()
    private val _imdbID = MutableLiveData<String>()

    init {
        startUpdate.value = false
    }

    val _movieItem: LiveData<MovieItem> = startUpdate.switchMap { update ->
        if (update) {
            _imdbID.value?.let {
                viewModelScope.launch {
                    favoriteService.refreshSelectedFavorite(it)
                }
            }
        }
        favoriteService.observeMovieItems().distinctUntilChanged().switchMap { filterFavorite(it) }
    }

    val movieItem: LiveData<MovieItem> = Transformations.map(_movieItem) {
        _movieItem.value
    }

    private fun filterFavorite(it: MovieItem): LiveData<MovieItem> {
        return MutableLiveData<MovieItem>(it)
    }

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun start(imdbID: String?) {
        if(TextUtils.isEmpty(imdbID)){
            return
        }
        if (startUpdate.value == true || imdbID == _imdbID.value) {
            return
        }
        startUpdate.value = true
        _imdbID.value = imdbID
    }

    private fun onMovieItem(data: MovieItem): MovieItem {
        return data
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

    fun updateFavoriteStatus() {
        _imdbID.value?.let {
            val data = movieItem.value
            data?.let {
                deleteMovieItem(imdbID = it.imdbID)
            } ?: addNewMovieItem(it)
        }
    }

    private fun deleteMovieItem(imdbID: String) = viewModelScope.launch {
        favoriteService.deleteMovieItem(imdbID)
    }

    private fun addNewMovieItem(imdbID: String) = viewModelScope.launch {
        val details = details.value
        details?.let {
            val movieItem = MovieItem(title = it.title, year = it.year, imdbID = imdbID, director = it.director, poster = it.poster)
            favoriteService.saveMovieItem(movieItem)
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}