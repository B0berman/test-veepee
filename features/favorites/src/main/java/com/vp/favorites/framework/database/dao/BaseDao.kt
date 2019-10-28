package com.vp.favorites.framework.database.dao

import androidx.room.*
import io.reactivex.Completable

@Dao
abstract class BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(type: T) : Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(type: T) : Completable

    @Delete
    abstract fun delete(type: T) : Completable
}
