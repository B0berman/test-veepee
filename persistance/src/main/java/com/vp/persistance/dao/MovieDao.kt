/*
 * Created by Alexis Rodriguez Paret on 3/13/20 8:57 PM
 * Copyright (c) 2020. All rights reserved. Heliocor (alexis.rodriquez@heliocor.com)
 * Last modified 3/13/20 7:59 PM
 *
 */

package com.vp.persistance.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.persistance.model.MovieModel

/**
 * Created by Alexis Rodríguez Paret on 2020-03-12.
 */
@Dao
interface MovieDao {

    @Query("SELECT * from favorite_table")
    fun getAll(): LiveData<List<MovieModel>>

    @Query("SELECT * from favorite_table where imdbID = :id LIMIT 1")
    fun getByimdbID(id: String): LiveData<MovieModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: MovieModel)

    @Query("DELETE FROM favorite_table where imdbID = :id")
    suspend fun delete(id: String)

}