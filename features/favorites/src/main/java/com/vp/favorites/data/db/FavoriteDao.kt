package com.vp.favorites.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.favorites.data.models.db.FavoriteDB
import com.vp.favorites.data.models.db.ID
import com.vp.favorites.data.models.db.tableName
import io.reactivex.Flowable

@Dao
interface FavoriteDao {
  @Query("SELECT * FROM $tableName")
  fun getAllRx(): Flowable<List<FavoriteDB>>

  @Query("SELECT EXISTS (SELECT 1 FROM $tableName WHERE $ID=:id)")
  fun isFavorite(id: String): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(item: FavoriteDB)

  @Delete
  fun delete(item: FavoriteDB)
}