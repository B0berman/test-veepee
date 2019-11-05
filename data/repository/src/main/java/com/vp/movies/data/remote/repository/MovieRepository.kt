package com.vp.movies.data.remote.repository

import com.vp.movies.data.local.dao.MovieDao
import com.vp.movies.data.model.MovieEntity
import com.vp.movies.data.remote.retrofit.service.MovieService
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

interface MovieRepository {
    fun search(query: String, page: Int): Single<List<MovieEntity>>
    fun get(imdbID: String): Single<MovieEntity>
    fun addToFavorites(entity: MovieEntity): Completable
    fun removeFromFavorites(imdbID: String): Completable
    fun isFavorite(imdbID: String): Maybe<Boolean>
}

class MovieRepositoryImpl @Inject internal constructor(
        private val movieService: MovieService,
        private val movieDao: MovieDao
) : MovieRepository {

    override fun search(query: String, page: Int): Single<List<MovieEntity>> {
        return movieService.search(query, page).map { it.search }
    }

    override fun get(imdbID: String): Single<MovieEntity> {
        return movieService.get(imdbID)
    }

    override fun isFavorite(imdbID: String): Maybe<Boolean> {
        return movieDao.find(imdbID).map { true }
    }

    override fun addToFavorites(entity: MovieEntity): Completable {
        return movieDao.insert(entity)
    }

    override fun removeFromFavorites(imdbID: String): Completable {
        return movieDao.remove(imdbID)
    }
}