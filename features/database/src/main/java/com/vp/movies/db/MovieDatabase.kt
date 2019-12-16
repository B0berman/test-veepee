package com.vp.movies.db

import com.squareup.sqldelight.Query
import javax.inject.Inject

class MovieDatabase @Inject constructor(val db: MovieDB) {

    fun getFavoriteMovies() : ArrayList<Movie> {
        var movies: ArrayList<Movie> = arrayListOf()

        val favoriteMovieTableQueries: Query<FavoriteMovieTable> = db.favoriteMovieTableQueries.selectAll()
        favoriteMovieTableQueries.executeAsList().forEach { favoriteMovieQuery ->

            val movie = Movie(
                    favoriteMovieQuery.id,
                    favoriteMovieQuery.title,
                    favoriteMovieQuery.year,
                    favoriteMovieQuery.runtime,
                    favoriteMovieQuery.director,
                    favoriteMovieQuery.plot,
                    favoriteMovieQuery.poster
            )
            movies.add(movie)
        }

        return movies
    }

    fun addFavoriteMovie(movie: Movie) {
        db.favoriteMovieTableQueries.insert(movie.id, movie.title, movie.year, movie.runtime, movie.director, movie.plot, movie.poster)
    }

    fun removeFavoriteMovie(id: String) {
        db.favoriteMovieTableQueries.deleteById(id)
    }

    fun cleanFavoriteMovies() {
        db.favoriteMovieTableQueries.deleteAll()
    }
}