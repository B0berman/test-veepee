package com.vp.favorites.service.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.favorites.model.MovieItem

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies_table")
    fun observeMovieItems(): LiveData<List<MovieItem>>

    @Query("SELECT * FROM movies_table")
    suspend fun getMovieItems(): List<MovieItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavorite(movie: MovieItem): Long

    @Query("SELECT * from movies_table")
    fun observeMovies() : LiveData<List<MovieItem>>

    @Query("SELECT * FROM movies_table WHERE imdbID = :imdbID")
    fun observeMovieItemById(imdbID: String): LiveData<MovieItem>

    @Query("SELECT * FROM movies_table WHERE imdbID = :imdbID")
    suspend fun getMovieItemById(imdbID: String): MovieItem?

    @Query("DELETE FROM movies_table WHERE imdbID = :imdbID")
    suspend fun deleteMovieItemById(imdbID: String): Int
}