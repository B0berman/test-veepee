package com.vp.storage.database

import androidx.room.*
import com.vp.storage.model.MovieDetailDB

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieDetailDB)

    @Query("SELECT * FROM MovieDetailDB WHERE imdbID = :imdbID")
    suspend fun findMovieByTitle(imdbID: String): MovieDetailDB?

    @Query("SELECT * FROM MovieDetailDB")
    suspend fun getFavoriteMovies(): List<MovieDetailDB>

    @Delete
    suspend fun deleteMovie(movie: MovieDetailDB)
}