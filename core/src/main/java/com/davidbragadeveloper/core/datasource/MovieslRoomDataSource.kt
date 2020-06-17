package com.davidbragadeveloper.core.datasource

import com.davidbragadeveloper.core.database.dao.MovieDao
import com.davidbragadeveloper.core.mapper.toDetailDomain
import com.davidbragadeveloper.core.mapper.toFavoritesDomain
import com.davidbragadeveloper.core.mapper.toRoom
import com.vp.detail.localdatasource.DetailsLocalDataSource
import com.vp.detail.model.MovieDetail
import com.vp.favorites.localdatasource.FavoritesLocalDataSource
import com.vp.favorites.model.MovieFavorite
import javax.inject.Inject

class MovieslRoomDataSource @Inject constructor(
        private val movieDao: MovieDao
): DetailsLocalDataSource, FavoritesLocalDataSource {

    override fun insertMovie(movie: MovieDetail): Boolean = movieDao.insert(movie.toRoom()) > 0

    override fun updateMovie(movie: MovieDetail): Boolean = movieDao.update(movie.toRoom()) > 0

    override fun getMovieById(movieId: String): MovieDetail? = movieDao.getMovieById(movieId)?.toDetailDomain()

    override fun loadFavorites(): List<MovieFavorite>  = movieDao.getFavorites().toFavoritesDomain()

}