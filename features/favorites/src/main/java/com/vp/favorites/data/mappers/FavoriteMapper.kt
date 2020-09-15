package com.vp.favorites.data.mappers

import com.vp.favorites.data.models.db.FavoriteDB
import com.vp.favorites.domain.model.FavoriteItem

fun List<FavoriteDB>.toDomain() = map { it.toDomain() }

fun FavoriteDB.toDomain() = FavoriteItem(
    id = id,
    image = favoriteImage,
    title = favoriteTitle
)

fun FavoriteItem.toDB() = FavoriteDB(
    id = id,
    favoriteImage = image,
    favoriteTitle = title
)