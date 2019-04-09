package com.vp.databases

import android.app.Application
import androidx.lifecycle.LiveData
import com.vp.databases.model.FavoriteMovie
import java.util.concurrent.Executors


class FavoriteMovieRepository(application: Application) {

    var dao: FavoriteMovieDao = AppDatabase.getInstance(application).favoriteMovieDao()
    var favoriteMovieList: LiveData<List<FavoriteMovie>>

    init {
        favoriteMovieList = dao.getAll()
    }

    fun isFavoriteMovieExit(imdbID: String): Boolean {
        return dao.favoriteMovieExist(imdbID) == 1
    }

    fun addToFavoriteMovie(imdbID: String, title: String, year: String, poster: String) {
        val favoriteMovie = FavoriteMovie()
        favoriteMovie.title = title
        favoriteMovie.imdbID = imdbID
        favoriteMovie.year = year
        favoriteMovie.poster = poster
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            dao.insert(favoriteMovie)
        }
    }

    fun deleteToFavoriteMovie(imdbID: String) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            dao.delete(imdbID)
        }
    }
}