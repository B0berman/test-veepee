package com.davidbragadeveloper.core

import android.graphics.Movie
import com.davidbragadeveloper.core.entity.MovieRoomEntity

interface MoviesRoomDataSource {

    fun getAllMovies(): List<MovieRoomEntity>
    fun getFavoriteMovies(): List<MovieRoomEntity>
    fun insertMovies(movies: List<MovieRoomEntity>):Boolean
    fun updateMovie(movie: MovieRoomEntity): Boolean

}