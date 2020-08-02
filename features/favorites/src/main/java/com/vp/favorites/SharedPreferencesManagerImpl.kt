package com.vp.favorites

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

private const val FAVORITE_PREF = "favorite"
private const val MOVIE_LIST_KEY = "movie_list"

class SharedPreferencesManagerImpl @Inject constructor(private val context: Context): SharedPreferencesManager {

    override fun getFavoriteString(): String {
        return getFavoritePreferences(context).getString(MOVIE_LIST_KEY, "")
    }

    override fun saveFavoriteString(favoriteListString: String) {
        getFavoritePreferences(context).edit().putString(MOVIE_LIST_KEY, favoriteListString).apply()
    }

    private fun getFavoritePreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(FAVORITE_PREF, Context.MODE_PRIVATE)
    }
}