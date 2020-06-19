package com.vp.data.local

import com.vp.data.model.MovieFavorite

interface FavoritesRepository {

    fun insert(movieFavorite: MovieFavorite)

    fun insertAll(vararg movieFavorites: MovieFavorite)

    fun getFromId(id: Int): MovieFavorite?

    fun getAll(): List<MovieFavorite>

    fun getAllFromIds(ids: IntArray): List<MovieFavorite>

    fun delete(movieFavorite: MovieFavorite)

    fun deleteAll()
}
