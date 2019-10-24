package com.vp.storage

import androidx.room.*

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