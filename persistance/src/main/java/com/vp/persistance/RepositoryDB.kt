package com.vp.persistance

import com.vp.persistance.model.FavoriteMovie

/**
 * Created by Uxio Lorenzo on 2019-09-09.
 */
interface RepositoryDB {

    fun toogleFavorite(movie: FavoriteMovie)

    fun getAll(): List<FavoriteMovie>

    fun findById(omdbId: String): FavoriteMovie?

}