package com.muhammedsafiulazam.photoalbum.feature.albumlist.listener

import com.vp.movies.db.Movie

/**
 * Created by Muhammed Safiul Azam on 19/11/2019.
 */

interface IFavoritesListener {
    fun onClickMovie(movie: Movie)
}