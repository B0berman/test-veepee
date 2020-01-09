package com.vp.favorites.service;


import com.vp.favorites.model.FetchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FetchService {

    @GET("/")
    Call<FetchResponse> search(@Query("i") String imdbID);
}

