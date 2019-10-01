package com.vp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.vp.database.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_movies")
    fun getAll(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite_movies WHERE imdb_id = :imdbID")
    fun isFavorite(imdbID : String) : LiveData<List<FavoriteEntity>>

    @Insert(onConflict = REPLACE)
    fun insert(favoriteMovie : FavoriteEntity)

    @Delete
    fun delete(favoriteMovie : FavoriteEntity)
}