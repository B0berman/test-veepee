package com.davidbragadeveloper.core.mapper

import com.davidbragadeveloper.core.entity.MovieRoomEntity
import com.vp.favorites.model.MovieFavorite

fun MovieRoomEntity.toFavoritesDomain() = MovieFavorite(
        id = id,
        title = title,
        poster = poster,
        year = year
)

fun List<MovieRoomEntity>.toFavoritesDomain() = map{ it.toFavoritesDomain() }