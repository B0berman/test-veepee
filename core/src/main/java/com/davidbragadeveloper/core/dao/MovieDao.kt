package com.davidbragadeveloper.core.dao

import androidx.room.*
import com.davidbragadeveloper.core.entity.MovieRoomEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM Movie")
    fun getAll(): List<MovieRoomEntity>

    @Query("SELECT * FROM Movie where favorite = 1")
    fun getFavorites() : List<MovieRoomEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movies: List<MovieRoomEntity>):List<Long>

    @Update
    fun update(movie: MovieRoomEntity ): Int

}