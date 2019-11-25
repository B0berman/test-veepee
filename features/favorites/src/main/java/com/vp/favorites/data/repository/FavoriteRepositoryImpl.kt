package com.vp.favorites.data.repository

import com.vp.favorites.core.FavoriteRepository
import com.vp.favorites.core.model.room.Movie
import com.vp.favorites.data.room.FavoriteDatabase
import com.vp.favorites.utils.composeWithBackgroundThreadSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(private val favoriteDatabase: FavoriteDatabase) : FavoriteRepository {


    override fun addMovieToFavorite(movie : Movie) : Completable = Completable.fromCallable { favoriteDatabase.getMovieDao().insert(movie) }
            .composeWithBackgroundThreadSchedulers()


    override fun getAllMovie() : Single<List<Movie>> {

        return Single.fromCallable {
            return@fromCallable favoriteDatabase.getMovieDao().getAllFavoriteMovie()
        }.composeWithBackgroundThreadSchedulers()

    }

    override fun removeFromFavorite(movieId : String) : Completable = Completable.fromCallable {
        favoriteDatabase.getMovieDao().deleteMovie(movieId)
    }.composeWithBackgroundThreadSchedulers()


    override fun isMovieInFavorite(movieId : String) : Single<Boolean> {

        return Single.fromCallable {
            val movieList = favoriteDatabase.getMovieDao().getAllFavoriteMovie()

            return@fromCallable movieList.find { it.movieId == movieId } != null
        }.composeWithBackgroundThreadSchedulers()
    }

}