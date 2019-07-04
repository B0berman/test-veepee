package com.vp.favorites.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.vp.core.favorites.FavoriteMoviesRepository
import com.vp.favorites.persistence.room.FavoritesDao
import com.vp.favorites.persistence.room.model.FavoriteMovieEntity
import java.util.*
import javax.inject.Inject

class FavoriteMoviesRoomRepository @Inject internal constructor(
        private val dao: FavoritesDao
) : FavoriteMoviesRepository {

    override fun isFavorite(movieId: String): LiveData<Boolean> {
        return Transformations.map(dao.findAllById(movieId)) { list -> list.isNotEmpty() }
    }

    override suspend fun setFavorite(movie: FavoriteMoviesRepository.Movie, favorite: Boolean) {
        when (favorite) {
            true -> dao.insert(movie.toEntity())
            false -> dao.deleteById(movie.id)
        }
    }

    private fun FavoriteMoviesRepository.Movie.toEntity() = FavoriteMovieEntity(
            id = id,
            dateAdded = Date(),
            title = title,
            year = year,
            director = director,
            poster = poster
    )
}