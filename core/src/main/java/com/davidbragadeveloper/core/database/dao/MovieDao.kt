package com.davidbragadeveloper.core.database.dao


import androidx.room.*
import com.davidbragadeveloper.core.entity.MovieRoomEntity


@Dao
interface MovieDao {

    @Query("SELECT id, title, year, poster FROM Movie where favorite = 1")
    fun getFavorites() : List<MovieRoomEntity>

    @Query("SELECT * FROM Movie where id = :id")
    fun getMovieById(id: String) : MovieRoomEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movie:MovieRoomEntity): Long

    @Update
    fun update(movie: MovieRoomEntity): Int

}