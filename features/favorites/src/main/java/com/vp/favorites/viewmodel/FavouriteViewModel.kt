package com.vp.favorites.viewmodel

import androidx.lifecycle.ViewModel
import com.vp.movie.abstraction.usecases.FavouriteMovieUseCase
import javax.inject.Inject

class FavouriteViewModel @Inject constructor(private val favouriteMovieUseCase: FavouriteMovieUseCase) : ViewModel() {
}