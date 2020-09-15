package com.vp.favorites.domain.usecases

import com.vp.favorites.domain.model.FavoriteItem
import com.vp.favorites.domain.repositories.FavoriteRepository
import io.reactivex.Flowable
import javax.inject.Inject

class GetAllFavoritesUseCase @Inject constructor(
  private val favoriteRepository: FavoriteRepository
) {

  fun execute(): Flowable<List<FavoriteItem>> {
    return favoriteRepository.getAllFavorites()
  }
}