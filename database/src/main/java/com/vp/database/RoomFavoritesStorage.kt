package com.vp.database

import android.arch.lifecycle.LiveData
import android.content.Context
import com.vp.database.model.FavoriteMovie
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class RoomFavoritesStorage @Inject constructor(context: Context) : FavoriteStorage {
    private val db: FavoriteDatabase = FavoriteDatabase.getInstance(context)

    override suspend fun containsMovie(movieId: String): Boolean {
        return async {
            db.favoritesDao().getAllWithId(movieId).isNotEmpty()
        }.await()
    }

    override suspend fun insertFavorite(favoriteMovie: FavoriteMovie): Long {
        return async {
            db.favoritesDao().insert(favoriteMovie)
        }.await()
    }

    override suspend fun removeFavorite(movieId: String): Int {
        return async {
            db.favoritesDao().delete(movieId)
        }.await()
    }

    override fun getFavoriteMovies(): LiveData<List<FavoriteMovie>> {
        return db.favoritesDao().getAll()
    }
}