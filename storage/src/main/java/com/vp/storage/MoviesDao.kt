package com.vp.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieDetailDB)

    @Query("SELECT * FROM MovieDetailDB")
    fun getFavoriteMovies(): List<MovieDetailDB>
}