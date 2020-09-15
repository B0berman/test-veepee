package com.vp.favorites.data.repo

import com.vp.favorites.data.db.AppDB
import com.vp.favorites.data.db.FavoriteDao
import com.vp.favorites.data.mappers.toDB
import com.vp.favorites.data.mappers.toDomain
import com.vp.favorites.domain.model.FavoriteItem
import com.vp.favorites.domain.repositories.FavoriteRepository
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
  private val dao: FavoriteDao
) : FavoriteRepository {

  override fun getAllFavorites(): Flowable<List<FavoriteItem>> {
    return AppDB.getInstance()
        .favoriteDao()
        .getAllRx()
        .subscribeOn(Schedulers.io())
        .map {
          it.toDomain()
        }
  }

  override fun addFavorite(item: FavoriteItem) = dao.insert(item.toDB())

  override fun removeFavorite(item: FavoriteItem) = dao.delete(item.toDB())

  override fun isFavorite(id: String) = AppDB.getInstance().favoriteDao().isFavorite(id) == 1
}