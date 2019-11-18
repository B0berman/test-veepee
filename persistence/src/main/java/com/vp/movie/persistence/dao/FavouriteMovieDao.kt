package com.vp.movie.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.movie.persistence.entities.MovieEntity
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface FavouriteMovieDao : BaseDao<MovieEntity> {

    @Query("SELECT * FROM favourites")
    fun getFavouriteMovies(): Observable<List<MovieEntity>>

    @Query("SELECT * FROM favourites WHERE imdbID LIKE :imdbID")
    fun getFavouriteMovieById(imdbID: String): Single<MovieEntity>

    @Query("DELETE FROM favourites WHERE imdbID = :imdbID")
    fun deleteFavouriteById(imdbID: String): Single<Int>

}