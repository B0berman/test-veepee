package com.vp.detail.model


sealed class MovieResponse {

    object Loading : MovieResponse()
    data class Error(val errorMessage : String) : MovieResponse()
    data class Success(val movieDetail : MovieDetail) : MovieResponse()

}

