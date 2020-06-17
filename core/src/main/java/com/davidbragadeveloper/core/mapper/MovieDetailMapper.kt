package com.davidbragadeveloper.core.mapper

import com.davidbragadeveloper.core.entity.MovieRoomEntity
import com.vp.detail.model.MovieDetail


fun MovieDetail.toRoom() = MovieRoomEntity(
        id = imdbID,
        title = title,
        director = director,
        plot = plot,
        poster = poster,
        runtime = runtime,
        year = year,
        favorite = favorite
)

fun MovieRoomEntity.toDetailDomain() = MovieDetail(
        imdbID = id,
        title = title,
        director = director ?: "",
        plot = plot ?: "",
        poster = poster,
        runtime = runtime ?: "",
        year = year,
        favorite = favorite ?: false
)