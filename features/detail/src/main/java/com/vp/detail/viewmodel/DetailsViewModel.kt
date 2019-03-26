package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.uk.missionlabs.db.FavouritesDB
import co.uk.missionlabs.db.Model.Favourite
import com.vp.detail.DetailActivity
import co.uk.missionlabs.db.Model.MovieDetail
import com.vp.detail.service.DetailService
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(private val detailService: DetailService) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val favourite: MutableLiveData<Boolean> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val favouritesDB: FavouritesDB = FavouritesDB();

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun favourite(): LiveData<Boolean> = favourite

    fun fetchDetails() {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(DetailActivity.queryProvider.getMovieId()).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                details.postValue(response?.body())

                response?.body()?.title?.let {
                    title.postValue(it)
                }

                favourite.postValue(favouritesDB.isFavourite(DetailActivity.queryProvider.getMovieId()))

                loadingState.value = LoadingState.LOADED
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

    fun toggleFavourite(){
        favouritesDB.toggleFavourite(Favourite(DetailActivity.queryProvider.getMovieId(),details.value))
        favourite.postValue(favouritesDB.isFavourite(DetailActivity.queryProvider.getMovieId()))
    }
}