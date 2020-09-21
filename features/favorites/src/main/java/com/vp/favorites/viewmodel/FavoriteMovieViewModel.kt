package com.vp.favorites.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.vp.detail.model.FavoriteMovie
import com.vp.favorites.database.FavoritesMoviesDb

class FavoriteMovieViewModel : ViewModel() {

    fun fetchFavoriteMovies(context: Context): List<FavoriteMovie> {
        return FavoritesMoviesDb.getInstance(context).favoriteMoviesDao().getFavoriteMovies()
    }

}