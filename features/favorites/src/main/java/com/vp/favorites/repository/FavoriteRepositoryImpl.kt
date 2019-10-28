package com.vp.favorites.repository

import com.vp.favorites.framework.database.dao.MovieDao
import com.vp.favorites.framework.database.mapper.MovieMapper
import com.vp.favorites.framework.database.model.MovieEntity
import com.vp.favorites.model.BasicMovie
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(private val moviesDao: MovieDao) :
    FavoriteRepository {
    override fun getAllMovies(): Single<List<BasicMovie>> {
        return moviesDao.getAllMovies()
            .map { MovieMapper.transformFromEntity(it) }
            .toSingle(emptyList())
    }

    override fun addMovie(movie: BasicMovie): Completable {
        return moviesDao.insert(MovieMapper.transformToEntity(movie))
    }

    override fun isMovieFav(movieID: String): Single<Boolean> {
        return moviesDao.isMovieFav(movieID)
            .toSingle(false)
    }

    override fun deleteMovie(movieID: String): Completable {
        return moviesDao.delete(MovieEntity(movieID))
    }

}