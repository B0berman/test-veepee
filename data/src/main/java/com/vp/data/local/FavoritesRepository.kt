package com.vp.data.local

import com.vp.data.model.MovieFavorite

interface FavoritesRepository {

    fun insert(movieFavorite: MovieFavorite)

    fun insertAll(vararg movieFavorites: MovieFavorite)

    fun getFromId(imdbID: String): MovieFavorite?

    fun getAll(): List<MovieFavorite>

    fun getAllFromIds(imdbIDs: Array<String>): List<MovieFavorite>

    fun delete(movieFavorite: MovieFavorite)

    fun deleteAll()
}
