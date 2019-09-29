package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.model.MovieDetailRealm
import com.vp.detail.service.DetailService
import io.realm.Realm
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(private val detailService: DetailService) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

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

    fun storeFavoriteMovie() {
        val realm: Realm = Realm.getDefaultInstance()
        realm.beginTransaction()

        val movieRealm = MovieDetailRealm()
        movieRealm.imdbId = DetailActivity.queryProvider.getMovieId()
        movieRealm.title = details.value?.title
        movieRealm.year = details.value?.year
        movieRealm.runtime = details.value?.runtime
        movieRealm.director = details.value?.director
        movieRealm.plot = details.value?.plot
        movieRealm.poster = details.value?.poster

        realm.copyToRealmOrUpdate(movieRealm)
        realm.commitTransaction()
    }

    fun removeFavoriteMovie() {
        val realm: Realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val favoriteMovie = realm.where(MovieDetailRealm::class.java).equalTo("imdbId", DetailActivity.queryProvider.getMovieId()).findAll()
        favoriteMovie.deleteAllFromRealm()
        realm.commitTransaction()
//        realm.delete(favoriteMovie);
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}