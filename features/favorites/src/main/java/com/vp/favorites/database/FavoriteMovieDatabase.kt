package com.vp.favorites.database

import com.vp.favorites.model.FavoriteMovie

/**
 * Created by Albert Vila Calvo on 2020-02-20.
 */
interface FavoriteMovieDatabase {

    fun getAll(): List<FavoriteMovie>
    fun add(favoriteMovie: FavoriteMovie)
    fun remove(movieId: String)
    fun contains(movieId: String): Boolean

}
