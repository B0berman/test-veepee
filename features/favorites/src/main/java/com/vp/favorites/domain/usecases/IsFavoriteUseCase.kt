package com.vp.favorites.domain.usecases

import com.vp.favorites.domain.repositories.FavoriteRepository
import io.reactivex.Flowable
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
  private val favoriteRepository: FavoriteRepository
) {

  fun execute(id: String): Boolean {
    return favoriteRepository.isFavorite(id)
  }
}