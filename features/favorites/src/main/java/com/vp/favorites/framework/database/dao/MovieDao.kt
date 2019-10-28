package com.vp.favorites.framework.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.vp.favorites.framework.database.Database.Companion.TABLE_MOVIES
import com.vp.favorites.framework.database.model.MovieEntity
import io.reactivex.Maybe

@Dao
abstract class MovieDao : BaseDao<MovieEntity>() {
    @Query("SELECT * FROM $TABLE_MOVIES")
    abstract fun getAllMovies(): Maybe<List<MovieEntity>>

    @Query("SELECT EXISTS(SELECT * FROM $TABLE_MOVIES WHERE id= :id)")
    abstract fun isMovieFav(id: String): Maybe<Boolean>
}