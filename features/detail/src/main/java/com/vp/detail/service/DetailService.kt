package com.vp.detail.service

import co.uk.missionlabs.db.Model.MovieDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DetailService {
    @GET("/")
    fun getMovie(@Query("i") imdbID: String): Call<MovieDetail>
}