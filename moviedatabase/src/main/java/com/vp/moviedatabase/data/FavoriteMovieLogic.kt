package com.vp.moviedatabase.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FavoriteMovieLogic(private val dao: FavoriteMovieDao) {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)



    @WorkerThread
    fun add(movie: Movie) {
        scope.launch {
            dao.insert(movie)
        }
    }
    @WorkerThread
    fun remove(movieId:String) {
        scope.launch {
            dao.deleteMovie(movieId)
        }
    }

    @WorkerThread
    suspend fun isFavorite(movieId:String):Boolean {
        var movie:Movie? = scope.async {
            dao.getMovie(movieId)
        }.await()

        if (movie == null) {
            return false
        }
        return true
    }

    @WorkerThread
    suspend fun getAll(): List<Movie> {
        return scope.async {
            dao.getAllMovies()
        }.await()
    }
}