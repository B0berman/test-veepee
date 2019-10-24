package com.vp.storage

import androidx.room.*

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieDetailDB)

    @Query("SELECT * FROM MovieDetailDB WHERE title = :movieTitle LIMIT 1")
    suspend fun findMovieByTitle(movieTitle: String): MovieDetailDB?

    @Query("SELECT * FROM MovieDetailDB")
    suspend fun getFavoriteMovies(): List<MovieDetailDB>

    @Delete
    suspend fun deleteMovie(movie: MovieDetailDB)
}