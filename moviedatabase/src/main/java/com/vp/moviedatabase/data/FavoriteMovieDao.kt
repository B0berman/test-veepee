package com.vp.moviedatabase.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getAllMovies():List<Movie>

    @Query("DELETE FROM movies WHERE id = :movieId")
    fun deleteMovie(movieId:String)

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovie(movieId:String): Movie?
}