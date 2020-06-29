package com.vp.favorite.service

import android.content.SharedPreferences

typealias FavDisposable = () -> Unit
typealias FavChangeCallback = (favorite: Boolean) -> Unit

interface FavoritesService {
    fun save(id: String, isFavorite: Boolean)
    fun getAllFavorite(): List<String>
    fun listenId(id: String, callback: FavChangeCallback): FavDisposable
}

class SharedPreferencesFavoritesService(private val sharedPreferences: SharedPreferences) : FavoritesService {
    override fun save(id: String, isFavorite: Boolean) {
        if (isFavorite) {
            sharedPreferences.edit().putBoolean(id, true).apply()
        } else {
            sharedPreferences.edit().remove(id).apply()
        }
    }

    override fun listenId(id: String, callback: FavChangeCallback): FavDisposable {
        callback(isFavorite(id))
        return listenChange { changedId, favorite ->
            if (changedId == id) {
                callback(favorite)
            }
        }
    }
    override fun getAllFavorite(): List<String> {
        return sharedPreferences.all
                .entries
                .map { it.key }
    }

    private fun isFavorite(id: String): Boolean {
        return sharedPreferences.getBoolean(id, false)
    }

    private fun listenChange(callback: (id: String, favorite: Boolean) -> Unit): FavDisposable {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            callback(key, sharedPreferences.getBoolean(key, false))
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        return {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }


}