package com.vp.favorites.persistence.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vp.favorites.persistence.room.FavoritesDatabase.ColumnNames.DATE_ADDED
import com.vp.favorites.persistence.room.FavoritesDatabase.ColumnNames.ID
import com.vp.favorites.persistence.room.FavoritesDatabase.TableNames.FAVORITE_MOVIES
import com.vp.favorites.persistence.room.model.FavoriteMovieEntity

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: FavoriteMovieEntity)

    @Transaction
    suspend fun deleteById(movieId: String): FavoriteMovieEntity? {
       return findById(movieId)?.also { delete(it) }
    }

    @Delete
    suspend fun delete(entity: FavoriteMovieEntity)

    @Query("SELECT * FROM $FAVORITE_MOVIES WHERE $ID = :movieId")
    suspend fun findById(movieId: String): FavoriteMovieEntity?

    @Query("SELECT * FROM $FAVORITE_MOVIES WHERE $ID = :movieId")
    fun findAllById(movieId: String): LiveData<List<FavoriteMovieEntity>>

    @Query("SELECT COUNT(*) FROM $FAVORITE_MOVIES")
    suspend fun size(): Int

    @Query("SELECT * FROM $FAVORITE_MOVIES ORDER BY $DATE_ADDED ASC")
    fun getAll(): LiveData<List<FavoriteMovieEntity>>
}