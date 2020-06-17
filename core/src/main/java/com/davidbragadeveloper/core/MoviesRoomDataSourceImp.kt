package com.davidbragadeveloper.core

import com.davidbragadeveloper.core.dao.MovieDao
import com.davidbragadeveloper.core.entity.MovieRoomEntity
import javax.inject.Inject

class MoviesRoomDataSourceImp @Inject constructor(
        private val movieDao: MovieDao
): MoviesRoomDataSource {

    override fun getAllMovies(): List<MovieRoomEntity> = movieDao.getAll()

    override fun getFavoriteMovies(): List<MovieRoomEntity> = movieDao.getFavorites()

    override fun insertMovies(movies: List<MovieRoomEntity>): Boolean = movieDao.insert(movies).let{it.isNotEmpty()}

    override fun updateMovie(movie: MovieRoomEntity): Boolean = movieDao.update(movie).let { it > 0 }
}