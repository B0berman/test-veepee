package com.vp.data.local

import androidx.room.*
import com.vp.data.model.MovieFavorite

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieFavorite: MovieFavorite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movieFavorite: MovieFavorite)

    @Query("SELECT * from favorites WHERE imdbID =:imdbID")
    fun getFromId(imdbID: String): MovieFavorite

    @Query("SELECT * from favorites WHERE imdbID IN (:imdbIDs)")
    fun getAllFromIds(imdbIDs: Array<String>): List<MovieFavorite>

    @Query("SELECT * from favorites ORDER BY timePersisted ASC")
    fun getAll(): List<MovieFavorite>

    @Delete
    fun delete(movieFavorite: MovieFavorite)

    @Query("DELETE FROM favorites")
    fun deleteAll()
}
