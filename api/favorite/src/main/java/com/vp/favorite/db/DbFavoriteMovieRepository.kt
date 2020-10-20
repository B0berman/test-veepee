package com.vp.favorite.db

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import com.vp.favorite.FavoriteMovieRepository
import com.vp.favorite.db.data.MovieQueries
import com.vp.favorite.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.vp.favorite.db.data.Movie as MovieEntity

class DbFavoriteMovieRepository @Inject internal constructor(
    private val movieQueries: MovieQueries
) : FavoriteMovieRepository {
    override val favoriteMovies: Flow<List<Movie>> = movieQueries.selectAll()
        .asFlow()
        .mapToList()
        .map { movies -> movies.map(MovieEntity::toMovie) }

    override fun movieFlow(id: String): Flow<Movie> {
        return movieQueries.getMovie(id)
            .asFlow()
            .mapToOne()
            .map { it.toMovie() }
    }

    override suspend fun getFavoriteMovies(): List<Movie> {
        return withContext(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                movieQueries.selectAll()
                    .executeAsList()
                    .map(MovieEntity::toMovie)
            }
        }
    }

    override suspend fun getMovie(id: String): Movie {
        return withContext(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                movieQueries.getMovie(id).executeAsOne().toMovie()
            }
        }
    }

    override suspend fun isFavorite(id: String): Boolean {
        return withContext(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                movieQueries.getMovie(id).executeAsOneOrNull() != null
            }
        }
    }

    override suspend fun addFavorite(movie: Movie) {
        withContext(Dispatchers.Default) {
            movieQueries.insertMovie(movie)
        }
    }

    override suspend fun removeFavorite(id: String) {
        withContext(Dispatchers.Default) {
            movieQueries.deleteMovie(id)
        }
    }
}
