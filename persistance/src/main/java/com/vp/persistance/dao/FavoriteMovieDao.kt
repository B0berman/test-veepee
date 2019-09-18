package com.vp.persistance.dao

import androidx.room.Dao
import androidx.room.Query
import com.vp.persistance.model.FavoriteMovie


/**
 * Created by Uxio Lorenzo on 2019-09-09.
 */
@Dao
interface FavoriteMovieDao :
    BaseDao<FavoriteMovie> {

    @Query("SELECT * FROM favoritemovie")
    fun getAll(): List<FavoriteMovie>

    @Query("SELECT * FROM favoritemovie WHERE omdbId = :omdbId")
    fun getById(omdbId: String): FavoriteMovie?

}