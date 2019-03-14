package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.moviedatabase.data.FavoriteMovieLogic
import com.vp.moviedatabase.data.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(private val detailService: DetailService,
                                           private val favoriteMovieLogic: FavoriteMovieLogic)
    : ViewModel(), StarButtonClickListener {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val starButtonState: MutableLiveData<StarButtonState> = MutableLiveData()

    private var movieId = ""
    private var loadingStateValue: LoadingState? = null

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun starButtonState(): LiveData<StarButtonState> = starButtonState

    fun fetchDetails(movieId: String) {
        if (loadingStateValue != LoadingState.LOADED) {
            this.movieId = movieId
            loadingState.value = LoadingState.IN_PROGRESS
            loadingStateValue = LoadingState.IN_PROGRESS
            detailService.getMovie(movieId)
                    .enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
                        override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                            details.postValue(response?.body())

                            response?.body()?.title?.let {
                                title.postValue(it)
                            }
                            loadingState.value = LoadingState.LOADED
                            loadingStateValue = LoadingState.LOADED
                        }

                        override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                            details.postValue(null)
                            loadingState.value = LoadingState.ERROR
                            loadingStateValue = LoadingState.ERROR
                        }
                    })
        }

    }


    override fun onStarButtonClick() {
        uiScope.launch {
            if (favoriteMovieLogic.isFavorite(movieId)) {
                favoriteMovieLogic.remove(movieId)
                starButtonState.postValue(StarButtonState.DEACTIVATED)
            } else {
                favoriteMovieLogic.add(details.value!!.toDbFormatWithId(movieId))
                starButtonState.postValue(StarButtonState.ACTIVATED)
            }
        }
    }

    fun requestStarButtonState() {
        uiScope.launch {
            var state = StarButtonState.DEACTIVATED
            if (favoriteMovieLogic.isFavorite(movieId)) {
                state = StarButtonState.ACTIVATED
            }
            starButtonState.setValue(state)
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }

    enum class StarButtonState {
        ACTIVATED, DEACTIVATED
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }
}

fun MovieDetail.toDbFormatWithId(movieId: String): Movie {
    return Movie(movieId, title, plot, director, runtime, year, poster)
}