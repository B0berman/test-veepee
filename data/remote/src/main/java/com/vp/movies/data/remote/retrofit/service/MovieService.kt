package com.vp.movies.data.remote.retrofit.service

import com.vp.movies.data.model.MovieEntity
import com.vp.movies.data.model.SearchResultEntity
import io.reactivex.Single
import retrofit2.http.*

interface MovieService {

    @GET("/")
    fun search(
        @Query("s") query: String,
        @Query("page") page: Int
    ): Single<SearchResultEntity<MovieEntity>>

    @GET("/")
    fun get(@Query("i") imdbID: String): Single<MovieEntity>
}
