package com.vp.favorites.repository

import androidx.lifecycle.LiveData
import android.app.Application
import com.vp.database.AppDatabase
import com.vp.database.dao.FavoriteDao
import com.vp.database.entity.FavoriteEntity
import javax.inject.Inject
import kotlin.concurrent.thread


class FavoriteRepository @Inject internal constructor(application: Application) {

    private var mFavoriteDao: FavoriteDao?
    private var favoriteMovies : LiveData<List<FavoriteEntity>>?

    init {
        val db = AppDatabase.getDatabase(application)
        mFavoriteDao = db?.favoriteDao()
        favoriteMovies = mFavoriteDao?.getAll()
    }

    fun getAll() : LiveData<List<FavoriteEntity>>? {
        return favoriteMovies
    }
    fun insert(movie : FavoriteEntity) {
        thread {
            mFavoriteDao?.insert(movie)
        }
    }
}