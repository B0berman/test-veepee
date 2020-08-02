package com.vp.favorites

interface SharedPreferencesManager {
    fun getFavoriteString(): String
    fun saveFavoriteString(favoriteListString: String)
}