package com.vp.persistance

import com.vp.persistance.dao.FavoriteMovieDao
import com.vp.persistance.model.FavoriteMovie
import javax.inject.Inject

/**
 * Created by Uxio Lorenzo on 2019-09-09.
 */

class MovieDataSource @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
): RepositoryDB {

    override fun findById(omdbId: String): FavoriteMovie?
            = favoriteMovieDao.getById(omdbId)

    override fun toogleFavorite(movie: FavoriteMovie) {
        favoriteMovieDao.getById(movie.omdbId)?.let {
            favoriteMovieDao.delete(it)
        } ?: favoriteMovieDao.insert(movie)

    }

    override fun getAll(): List<FavoriteMovie> = favoriteMovieDao.getAll()

}