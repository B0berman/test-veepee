package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.model.MovieDetailMapper
import com.vp.detail.service.DetailService
import com.vp.storage.DataBaseManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
        private val detailService: DetailService,
        private val databaseManager: DataBaseManager,
        private val movieDetailMapper: MovieDetailMapper
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val isFavourite: MutableLiveData<Boolean> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    var movieId: String = ""

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun favourite(): LiveData<Boolean> = isFavourite

    private var disposables: CompositeDisposable = CompositeDisposable()

    fun fetchDetails() {
        disposables.add(databaseManager.getMovieDatabase().movieDoa().findByImdbID(movieId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    isFavourite.value = true
                }) {
                    isFavourite.value = false
                })
        loadingState.value = LoadingState.IN_PROGRESS
        // ANSWER Memory leaks  was here. Passing Activity instance to view model is not allowed
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

    fun setFavourite() {
        if (isFavourite.value == true)
            disposables.add(
                    databaseManager.getMovieDatabase().movieDoa()
                            .deleteByImdbID(movieId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                isFavourite.value = false
                            })
        else
            details.value?.run {
                disposables.add(databaseManager.getMovieDatabase().movieDoa().insertAll(
                        movieDetailMapper.toMovieItemDTO(movieId, this)
                )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            isFavourite.value = true
                        })
            }
    }


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}