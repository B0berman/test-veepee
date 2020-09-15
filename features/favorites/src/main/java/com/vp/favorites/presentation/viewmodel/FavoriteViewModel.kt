package com.vp.favorites.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.domain.model.FavoriteItem
import com.vp.favorites.domain.usecases.GetAllFavoritesUseCase
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
  getAllFavoritesUseCase: GetAllFavoritesUseCase
): ViewModel() {

  private var _favoritesItems = MutableLiveData<List<FavoriteItem>>()
  val favoritesItems: LiveData<List<FavoriteItem>>
    get() = _favoritesItems

  init {
    getAllFavoritesUseCase.execute()
        .subscribeOn(Schedulers.computation())
        .subscribe {
          _favoritesItems.postValue(it)
        }
  }
}