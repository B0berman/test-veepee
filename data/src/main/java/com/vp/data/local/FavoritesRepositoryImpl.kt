package com.vp.data.local

import com.vp.data.model.MovieFavorite
import java.util.*
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(private val favoritesDao: FavoritesDao) : FavoritesRepository {

    override fun insert(movieFavorite: MovieFavorite) {
        movieFavorite.timePersisted = Date()
        favoritesDao.insert(movieFavorite)
    }

    override fun insertAll(vararg movieFavorites: MovieFavorite) {
        movieFavorites.forEach { movie -> movie.timePersisted = Date() }
        favoritesDao.insertAll(*movieFavorites)
    }

    override fun getFromId(id: Int): MovieFavorite? {
        return favoritesDao.getFromId(id)
    }

    override fun getAll(): List<MovieFavorite> {
        return favoritesDao.getAll()
    }

    override fun getAllFromIds(ids: IntArray): List<MovieFavorite> {
        return favoritesDao.getAllFromIds(ids)
    }

    override fun delete(movieFavorite: MovieFavorite) {
        favoritesDao.delete(movieFavorite)
    }

    override fun deleteAll() {
        favoritesDao.deleteAll()
    }
}
