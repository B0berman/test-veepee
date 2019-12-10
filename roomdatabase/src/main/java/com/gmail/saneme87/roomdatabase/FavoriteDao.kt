package com.gmail.saneme87.roomdatabase

import androidx.room.*
import com.gmail.saneme87.roomdatabase.model.FavoriteMovie

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteMovie: FavoriteMovie)

    @Update
    suspend fun update(vararg favoriteMovie: FavoriteMovie)

    @Delete
    suspend fun delete(vararg favoriteMovie: FavoriteMovie)

    @Query("SELECT * FROM favorite")
    suspend fun getAllFavorites(): List<FavoriteMovie>

    @Query("SELECT * FROM favorite WHERE id = :id")
    suspend fun getFavorite(id: String): FavoriteMovie?
}