package com.vp.databases

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vp.databases.model.FavoriteMovie

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favoriteMovie")
    fun getAll(): LiveData<List<FavoriteMovie>>

    @Query("SELECT EXISTS (SELECT * FROM favoriteMovie WHERE imdbID LIKE :imdbIDFilter LIMIT 1)")
    fun favoriteMovieExist(imdbIDFilter: String): Int

    @Insert
    fun insert(vararg favoriteMovie: FavoriteMovie)

    @Query("DELETE FROM favoriteMovie WHERE imdbID = :imdbIDFilter")
    fun delete(imdbIDFilter: String)

    @Delete
    fun delete(favoriteMovie: FavoriteMovie)
}