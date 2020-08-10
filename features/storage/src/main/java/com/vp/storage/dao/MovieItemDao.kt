package com.vp.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vp.storage.model.MovieItemDTO
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MovieItemDao {
    @Query("SELECT * FROM movie")
    fun getAll(): Single<List<MovieItemDTO>>

    @Query("SELECT * FROM movie WHERE imdbID = :imdbID")
    fun findByImdbID(imdbID: String): Single<MovieItemDTO?>

    @Insert
    fun insertAll(vararg movie: MovieItemDTO) : Completable

    @Delete
    fun delete(movie: MovieItemDTO): Completable

    @Query("DELETE FROM movie WHERE imdbID = :imdbID")
    fun deleteByImdbID(imdbID: String): Completable
}