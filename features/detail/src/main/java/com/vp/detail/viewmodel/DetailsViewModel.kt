package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.schibsted.android.rocket.utils.observeOnce
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.model.toFavMovie
import com.vp.detail.service.DetailService
import retrofit2.Call
import retrofit2.Response
import saha.tushar.common.db.FavMovie
import saha.tushar.common.db.FavMovieRepository
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
        private val detailService: DetailService,
        private val dbFavMovieRepository: FavMovieRepository
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val favList: LiveData<List<FavMovie>> = dbFavMovieRepository.getFavourites()
    val isFavourite: MutableLiveData<Boolean> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    init {
        favList.observeOnce {
            it.forEach { favMovie ->
                if (favMovie.imdbId == DetailActivity.queryProvider.getMovieId()) {
                    isFavourite.postValue(true)
                    return@observeOnce
                }
            }
            isFavourite.postValue(false)
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

    fun isFavourite(): Boolean = isFavourite.value ?: false

    fun addFavourite() {
        details.value?.let {
            dbFavMovieRepository
                    .addFavourite(it.toFavMovie(DetailActivity.queryProvider.getMovieId())) { movieRoomDbId ->
                        if (movieRoomDbId > -1) {
                            isFavourite.postValue(true)
                        }
                    }
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}