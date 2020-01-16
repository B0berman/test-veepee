package com.vp.persistence.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.vp.persistence.BuildConfig
import com.vp.persistence.extension.fromJson
import com.vp.persistence.model.FavoriteData
import com.vp.persistence.storage.AppPreferences.Companion.SHARED_FAVORITE_MOVIE_LIST
import javax.inject.Inject

interface AppPreferences {

    companion object {
        const val SHARED_FAVORITE_MOVIE_LIST = "SHARED_FAVORITE_MOVIE_LIST"
    }

    fun isMovieFavorite(movieId: String): Boolean
    fun getFavoriteMovieList(): List<FavoriteData>
    fun setMovieFavorite(item: FavoriteData)
    fun removeMovieFavorite(movieId: String)
}

class AppPreferencesStorage @Inject constructor(context: Context) : AppPreferences {

    private val storage: SharedPreferences =
        context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    override fun getFavoriteMovieList(): List<FavoriteData> {
        val string = storage.getString(SHARED_FAVORITE_MOVIE_LIST, "")
        return Gson().fromJson<List<FavoriteData>>(string) ?: listOf()
    }

    override fun isMovieFavorite(movieId: String): Boolean {
        return getFavoriteMovieList().find { it.id == movieId} != null
    }

    @SuppressLint("ApplySharedPref")
    override fun setMovieFavorite(item: FavoriteData) {
        val list = getFavoriteMovieList().toMutableList()
        list.add(item)
        storage.edit().putString(SHARED_FAVORITE_MOVIE_LIST, Gson().toJson(list)).commit()
    }

    @SuppressLint("ApplySharedPref")
    override fun removeMovieFavorite(movieId: String){
        val list = getFavoriteMovieList().toMutableList()
        list.removeAll {
            it.id == movieId
        }
        storage.edit().putString(SHARED_FAVORITE_MOVIE_LIST, Gson().toJson(list)).commit()
    }
}

