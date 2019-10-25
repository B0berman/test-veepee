package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.model.FavoriteMovie
import com.vp.storage.database.MoviesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FavoriteViewModel @Inject constructor(
        private val moviesDao: MoviesDao
) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    private val favoriteMovies: MutableLiveData<List<FavoriteMovie>> = MutableLiveData()

    fun favoriteMovies(): LiveData<List<FavoriteMovie>> = favoriteMovies

    fun fetchMovies() {
        launch {
            favoriteMovies.value = moviesDao.getFavoriteMovies().map {movieDB ->
                movieDB.let {
                    FavoriteMovie(
                            title = it.title,
                            year = it.year,
                            runtime = it.runtime,
                            director = it.director,
                            plot = it.plot,
                            poster = it.poster,
                            imdbID = it.imdbID
                    )
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}