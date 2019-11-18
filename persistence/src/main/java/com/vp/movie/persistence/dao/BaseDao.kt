package com.vp.movie.persistence.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import io.reactivex.Single

interface BaseDao<T> {

    @Insert
    fun insert(item: T): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(item: T): Single<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(item: T): Single<Long>

    @Insert
    fun insertAll(items: List<T>): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllOrReplace(items: List<T>): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllOrIgnore(items: List<T>): Single<List<Long>>

    @Update
    fun update(item: T): Single<Int>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateOrReplace(item: T): Single<Int>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateOrIgnore(item: T): Single<Int>

    @Update
    fun updateAll(items: List<T>): Single<Int>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAllOrReplace(items: List<T>): Single<Int>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateAllOrIgnore(items: List<T>): Single<Int>

    @Delete
    fun delete(item: T): Single<Int>

    @Delete
    fun deleteAll(items: List<T>): Single<Int>

}