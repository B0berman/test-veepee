package com.vp.favorites.service

import com.vp.favorites.model.FavoriteItem
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface FavoritesService {

    @GET("/")
    fun getMovie(@Query("i") imdbID: String): Observable<FavoriteItem>
}