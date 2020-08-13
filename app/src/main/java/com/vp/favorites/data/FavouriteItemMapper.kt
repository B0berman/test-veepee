package com.vp.favorites.data

import com.vp.favorites.model.FavouriteItem
import com.vp.storage.dbmodel.DbMovie

fun DbMovie.toFavouriteItem(): FavouriteItem = FavouriteItem(
        title = title,
        year = year,
        imdbID = imdbID,
        poster = poster
)
