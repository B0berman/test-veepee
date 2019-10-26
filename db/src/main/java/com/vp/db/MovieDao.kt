package com.vp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun retrieve(): LiveData<List<MovieDB>>

    @Query("SELECT * FROM movie WHERE id= :id")
    fun retrieve(id: String): LiveData<MovieDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movie: MovieDB)
}
