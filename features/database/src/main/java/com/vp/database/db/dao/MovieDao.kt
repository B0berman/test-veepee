package com.vp.database.db.dao

import androidx.room.*
import com.vp.database.beans.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM favorite_movies")
    fun allFavoriteMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteShow: Movie)

    @Delete
    fun remove(favoriteShow: Movie)
}