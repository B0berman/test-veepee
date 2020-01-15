package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.model.ListItem
import com.vp.favorites.viewmodel.FavoriteFetchResult.Companion.inProgress
import com.vp.favorites.viewmodel.FavoriteFetchResult.Companion.onSuccess
import com.vp.persistence.storage.AppPreferences
import javax.inject.Inject

class ListViewModel @Inject internal constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {
    private val liveData = MutableLiveData<FavoriteFetchResult>()

    fun observeMovies(): LiveData<FavoriteFetchResult> {
        return liveData
    }

    fun fetchFavoriteList() {
        liveData.value = inProgress()

        val list = appPreferences.getFavoriteMovieList().map {
            ListItem(title = it, year = "", imdbID = "", poster = "")
        }
        liveData.postValue(onSuccess(items = list))
    }
}