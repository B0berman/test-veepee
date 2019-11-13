package com.vp.movies.data.remote.repository

import com.vp.movies.data.local.dao.MovieDao
import com.vp.movies.data.model.MovieEntity
import com.vp.movies.data.model.SearchResultEntity
import com.vp.movies.data.remote.retrofit.service.MovieService
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

interface MovieRepository {
    fun search(query: String, page: Int): Single<SearchResultEntity<MovieEntity>>
    fun get(imdbID: String): Single<MovieEntity>
    fun getFavorites(): Flowable<List<MovieEntity>>
    fun addToFavorites(entity: MovieEntity): Completable
    fun removeFromFavorites(imdbID: String): Completable
    fun isFavorite(imdbID: String): Maybe<MovieEntity>
}

class MovieRepositoryImpl @Inject internal constructor(
        private val movieService: MovieService,
        private val movieDao: MovieDao
) : MovieRepository {

    override fun search(query: String, page: Int): Single<SearchResultEntity<MovieEntity>> {
        return movieService.search(query, page)
    }

    override fun get(imdbID: String): Single<MovieEntity> {
        return movieService.get(imdbID)
    }

    override fun getFavorites(): Flowable<List<MovieEntity>> {
        return movieDao.findAll()
    }

    override fun isFavorite(imdbID: String): Maybe<MovieEntity> {
        return movieDao.find(imdbID)
    }

    override fun addToFavorites(entity: MovieEntity): Completable {
        return movieDao.insert(entity)
    }

    override fun removeFromFavorites(imdbID: String): Completable {
        return movieDao.remove(imdbID)
    }
}