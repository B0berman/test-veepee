package com.vp.favorites.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.favorites.core.model.room.Movie


@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getAllFavoriteMovie() : List<Movie>

    @Query("DELETE FROM movie")
    fun clear() : Int

    @Query("DELETE FROM movie WHERE movieId=:movieId")
    fun deleteMovie(movieId : String) : Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movie: Movie)

}