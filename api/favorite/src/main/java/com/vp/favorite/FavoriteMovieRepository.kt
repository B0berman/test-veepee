package com.vp.favorite

import com.vp.favorite.model.Movie
import kotlinx.coroutines.flow.Flow

interface FavoriteMovieRepository {
    val favoriteMovies: Flow<List<Movie>>
    fun movieFlow(id: String): Flow<Movie>
    suspend fun isFavorite(id: String): Boolean
    suspend fun addFavorite(movie: Movie)
    suspend fun removeFavorite(id: String)
}

suspend fun FavoriteMovieRepository.toggleFavorite(movie: Movie) {
    if (isFavorite(movie.id)) {
        removeFavorite(movie.id)
    } else {
        addFavorite(movie)
    }
}
