package com.vp.detail.di

import com.vp.detail.DetailActivity
import com.vp.detail.service.DetailService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class DetailNetworkModule {
    @Provides
    fun providesDetailService(retrofit: Retrofit): DetailService {
        return retrofit.create(DetailService::class.java)
    }

    @Provides
    fun retrieveIntentWithMovieId(activity: DetailActivity): String {
        return activity.intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }
}