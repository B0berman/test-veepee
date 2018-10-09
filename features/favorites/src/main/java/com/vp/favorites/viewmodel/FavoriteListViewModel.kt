package com.vp.favorites.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.SharedPreferences
import com.google.gson.Gson
import com.vp.favorites.model.FavoriteMovie
import com.vp.favorites.model.FavoriteResult
import com.vp.favorites.model.FavoriteState
import javax.inject.Inject

class FavoriteListViewModel @Inject constructor(private val sharedPreferences: SharedPreferences, private val gson: Gson) : ViewModel() {

    private val liveData = MutableLiveData<FavoriteResult>()

    fun observeFavorites(): LiveData<FavoriteResult> {
        return liveData
    }

    fun getFavoriteList() {
        liveData.value = FavoriteResult(FavoriteState.IN_PROGRESS, emptyMap())

        val resultMap = mutableMapOf<String, FavoriteMovie>()
        for (mutableEntry in sharedPreferences.all) {
            sharedPreferences.getString(mutableEntry.key, null)?.let { jsonMovie ->
                gson.fromJson(jsonMovie, FavoriteMovie::class.java)?.let { movie ->
                    resultMap.put(mutableEntry.key, movie)
                }
            }
        }

        liveData.value = FavoriteResult(FavoriteState.LOADED, resultMap)
    }
}