package com.vp.favorites.data.mappers

import com.vp.favorites.data.models.db.FavoriteDB
import com.vp.favorites.domain.model.FavoriteItem

fun List<FavoriteDB>.toDomain() = map { it.toDomain() }

fun FavoriteDB.toDomain() = FavoriteItem(
    id = id,
    poster = favoriteImage,
    title = favoriteTitle
)

fun FavoriteItem.toDB() = FavoriteDB(
    id = id,
    favoriteImage = poster,
    favoriteTitle = title
)