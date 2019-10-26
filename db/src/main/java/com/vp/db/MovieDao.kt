package com.vp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun retrieve(): LiveData<List<MovieDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movie: MovieDB)
}
