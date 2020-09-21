package com.vp.favorites.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vp.detail.model.FavoriteMovie

@Dao
interface FavoriteMoviesDao {
    @Query("SELECT * FROM FavoriteMovie")
    fun getFavoriteMovies(): List<FavoriteMovie>

    @Query("SELECT * FROM FavoriteMovie WHERE imdbID = :id")
    fun getFavoriteMovieById(id: String): FavoriteMovie?

    @Insert
    fun insertFavoriteMovie(vararg favoriteMovies: FavoriteMovie)

    @Delete
    fun deleteFavoriteMovie(favoriteMovie: FavoriteMovie)
}