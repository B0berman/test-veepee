package com.vp.favorites.domain.usecases

import com.vp.favorites.domain.model.FavoriteItem
import com.vp.favorites.domain.repositories.FavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
  private val favoriteRepository: FavoriteRepository
) {

  fun execute(item: FavoriteItem) {
    favoriteRepository.addFavorite(item)
  }
}