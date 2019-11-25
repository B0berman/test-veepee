package com.vp.detail.model

sealed class Events {
    data class GetMovieDetails(val movieId : String) : Events()
    data class AddOrRemoveFavorite(val movie : MovieDetail, val remove : Boolean = false) : Events()
    data class RefreshMovieFavoriteState(val movieId : String) : Events()
}