package com.vp.persistence.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.vp.persistence.BuildConfig
import com.vp.persistence.extension.fromJson
import com.vp.persistence.extension.getOrNullableCompat
import com.vp.persistence.storage.AppPreferences.Companion.SHARED_FAVORITE_MOVIE_LIST
import com.vp.persistence.storage.AppPreferences.Companion.SHARED_FCM_TOKEN
import javax.inject.Inject

interface AppPreferences {

    companion object {
        const val SHARED_FCM_TOKEN = "SHARED_FCM_TOKEN"
        const val SHARED_FAVORITE_MOVIE_LIST = "SHARED_FAVORITE_MOVIE_LIST"
    }

    fun getFCMToken(): String?
    fun setFCMToken(token: String)
    fun isMovieFavorite(movieId: String): Boolean
    fun setMovieFavorite(movieId: String, bool: Boolean)
}

class AppPreferencesStorage @Inject constructor(context: Context) : AppPreferences {

    private val storage: SharedPreferences =
        context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    override fun getFCMToken(): String? =
        storage.getString(SHARED_FCM_TOKEN, "")

    @SuppressLint("ApplySharedPref")
    override fun setFCMToken(token: String) {
        storage.edit().putString(SHARED_FCM_TOKEN, token).commit()
    }

    private fun getFavoriteMovieList(): Map<String, Boolean> {
        val string = storage.getString(SHARED_FAVORITE_MOVIE_LIST, "")
        return Gson().fromJson<Map<String, Boolean>>(string) ?: mapOf()
    }

    override fun isMovieFavorite(movieId: String): Boolean {
        return getFavoriteMovieList().getOrNullableCompat(movieId) ?: false
    }

    @SuppressLint("ApplySharedPref")
    override fun setMovieFavorite(movieId: String, bool: Boolean) {
        val map = getFavoriteMovieList().toMutableMap()
        map[movieId] = bool
        storage.edit().putString(SHARED_FAVORITE_MOVIE_LIST, Gson().toJson(map)).commit()
    }
}

