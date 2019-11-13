package com.vp.movies.data.local.dao

import androidx.room.*
import com.vp.movies.data.model.MovieEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Data Access Object for the excluded users table.
 */
@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun findAll(): Flowable<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movie: MovieEntity): Completable

    @Query("DELETE FROM movie WHERE imdbID= :imdbID")
    fun remove(imdbID: String): Completable

    @Query("SELECT * FROM movie WHERE imdbID= :imdbID")
    fun find(imdbID: String): Maybe<MovieEntity>
}
