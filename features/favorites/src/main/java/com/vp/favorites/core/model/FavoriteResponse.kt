package com.vp.favorites.core.model

sealed class FavoriteResponse {

    object Loading : FavoriteResponse()
    object Error : FavoriteResponse()
    object Success : FavoriteResponse()
}