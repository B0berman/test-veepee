package com.gmail.saneme87.roomdatabase

import androidx.room.*
import com.gmail.saneme87.roomdatabase.model.Favorite

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: Favorite)

    @Update
    suspend fun update(vararg favorite: Favorite)

    @Delete
    suspend fun delete(vararg favorite: Favorite)

    @Query("SELECT * FROM favorite")
    suspend fun getAllFavorites(): List<Favorite>

    @Query("SELECT * FROM favorite WHERE id = :id")
    suspend fun getFavorite(id: String): Favorite?
}