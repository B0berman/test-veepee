package com.vp.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.persistance.dao.FavoriteMovieDao
import com.vp.persistance.model.FavoriteMovie

/**
 * Created by Uxio Lorenzo on 2019-09-09.
 */
@Database(entities = [FavoriteMovie::class], version = 1, exportSchema = false)
abstract class MovieDB: RoomDatabase() {

    abstract fun favoriteMovieDao(): FavoriteMovieDao

}