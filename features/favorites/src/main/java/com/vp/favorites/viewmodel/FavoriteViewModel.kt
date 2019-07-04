package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vp.favorites.persistence.room.FavoritesDao
import com.vp.favorites.persistence.room.model.FavoriteMovieEntity
import com.vp.favorites.viewmodel.model.FavoriteMovie
import com.vp.favorites.viewmodel.model.FavoriteMovieDeletion
import kotlinx.coroutines.*
import javax.inject.Inject

internal class FavoriteViewModel @Inject internal constructor(private val dao: FavoritesDao) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val moviesList: LiveData<List<FavoriteMovie>> = Transformations
            .map(dao.getAll()) { entities -> entities.map { entity -> entity.toFavoriteMovie() } }

    private val deletions = MutableLiveData<FavoriteMovieDeletion>()

    override fun onCleared() {
        scope.cancel()
        super.onCleared()
    }

    fun getMoviesList(): LiveData<List<FavoriteMovie>> = moviesList

    fun getDeletions(): LiveData<FavoriteMovieDeletion> = deletions

    fun removeFromFavorites(movie: FavoriteMovie) {
        scope.launch {
            dao.deleteById(movie.id)
                    ?.also { entity -> deletions.postValue(entity.toDeletion()) }
        }
    }

    private fun FavoriteMovieEntity.toDeletion() = FavoriteMovieDeletion(
            movie = toFavoriteMovie(),
            undo = {
                scope.launch {
                    dao.insert(this@toDeletion)
                }
            },
            dismiss = {
                scope.launch(Dispatchers.Main) {
                    if (deletions.value?.movie == toFavoriteMovie()) {
                        deletions.value = null
                    }
                }
            }
    )

    private fun FavoriteMovieEntity.toFavoriteMovie() = FavoriteMovie(
            id = id,
            title = title,
            year = year,
            director = director,
            poster = poster?.takeUnless { it == NO_IMAGE }
    )

    private companion object {
        private const val NO_IMAGE = "N/A"
    }
}