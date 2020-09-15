package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.favorites.domain.model.FavoriteItem
import com.vp.favorites.domain.usecases.AddFavoriteUseCase
import com.vp.favorites.domain.usecases.IsFavoriteUseCase
import com.vp.favorites.domain.usecases.RemoveFavoriteUseCase
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
  private val detailService: DetailService,
  private val addFavoriteUseCase: AddFavoriteUseCase,
  private val isFavoriteUseCase: IsFavoriteUseCase,
  private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

  private val details: MutableLiveData<MovieDetail> = MutableLiveData()
  private val title: MutableLiveData<String> = MutableLiveData()
  private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

  fun title(): LiveData<String> = title

  fun details(): LiveData<MovieDetail> = details

  fun state(): LiveData<LoadingState> = loadingState

  fun fetchDetails() {
    loadingState.value = LoadingState.IN_PROGRESS
    detailService.getMovie(DetailActivity.queryProvider.getMovieId())
        .enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
          override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
            details.postValue(response?.body())

            response?.body()
                ?.title?.let {
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

  fun removeFavorite(id: String) {
    details.value?.let {
      removeFavoriteUseCase.execute(FavoriteItem(id, it.poster, it.title))
    }
  }

  fun addFavorite(id: String) {
    details.value?.let {
      addFavoriteUseCase.execute(FavoriteItem(id, it.poster, it.title))
    }
  }

  fun isFavorite(id: String) = isFavoriteUseCase.execute(id)

  enum class LoadingState {
    IN_PROGRESS,
    LOADED,
    ERROR
  }
}