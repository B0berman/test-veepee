package com.vp.data.local

import androidx.lifecycle.LiveData
import com.vp.data.model.MovieFavorite

interface FavoritesRepository {

    fun insert(movieFavorite: MovieFavorite)

    fun insertAll(vararg movieFavorites: MovieFavorite)

    fun getFromId(imdbID: String): MovieFavorite?

    fun getAllLiveData(): LiveData<List<MovieFavorite>>

    fun getAll(): List<MovieFavorite>

    fun getAllFromIds(imdbIDs: Array<String>): List<MovieFavorite>

    fun deleteFromId(imdbID: String)

    fun delete(movieFavorite: MovieFavorite)

    fun deleteAll()
}
