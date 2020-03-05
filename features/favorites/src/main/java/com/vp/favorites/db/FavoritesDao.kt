package com.vp.favorites.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.api.model.ListItem
import com.vp.api.model.ListItem.Companion.TABLE_NAME

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(item: ListItem)

    @Delete
    suspend fun remove(item:ListItem)

    @Query("DELETE FROM $TABLE_NAME WHERE imdbID = :id")
    suspend fun removeById(id: String)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): LiveData<List<ListItem>>

    @Query("SELECT EXISTS(SELECT 1 FROM $TABLE_NAME WHERE imdbID = :id LIMIT 1)")
    suspend fun existsWithId(id: String): Boolean
}
