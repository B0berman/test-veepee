package com.vp.favorites.model

data class FavoriteResult(val state: FavoriteState,
                          val favoriteMap: Map<String, FavoriteMovie>)