package com.vp.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.vp.database.model.FavoriteMovie

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    fun getAll(): LiveData<List<FavoriteMovie>>

    @Query("SELECT * FROM favorites WHERE id = :id")
    fun getAllWithId(id: String): List<FavoriteMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteMovie: FavoriteMovie): Long

    @Query("DELETE FROM favorites WHERE id = :id")
    fun delete(id: String): Int
}