package com.vp.favorites.viewmodel

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.model.FavoriteMovie
import com.vp.favorites.service.FavoriteService
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val service: FavoriteService) : ViewModel() {

    val liveData = MutableLiveData<List<FavoriteMovie>>()
    val state: MutableLiveData<State> = MutableLiveData()

    fun fetch() {
        state.value = State.LOADING
        AsyncTask.execute {
            val favoritesMovies = service.getFavoritesMovies()
            Handler(Looper.getMainLooper()).post {
                liveData.value = favoritesMovies
                state.value = if (favoritesMovies.isEmpty()) State.EMPTY else State.WITH_RESULT
            }
        }
    }

    enum class State {
        LOADING, EMPTY, WITH_RESULT
    }
}
