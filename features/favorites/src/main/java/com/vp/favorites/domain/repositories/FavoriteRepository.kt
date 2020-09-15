package com.vp.favorites.domain.repositories

import com.vp.favorites.domain.model.FavoriteItem
import io.reactivex.Flowable

interface FavoriteRepository {
  fun getAllFavorites(): Flowable<List<FavoriteItem>>
  fun addFavorite(id: FavoriteItem)
  fun removeFavorite(item: FavoriteItem)
  fun isFavorite(id: String): Boolean
}