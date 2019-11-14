package com.vp.movie.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.movie.persistence.entities.MovieEntity

@Dao
interface FavouriteMovieDao {

    @Query("SELECT * FROM favourites")
    fun getFavouriteMovies(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceMovie(movie: MovieEntity): Long


}