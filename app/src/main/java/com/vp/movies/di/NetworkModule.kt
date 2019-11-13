package com.vp.movies.di

import com.vp.movies.BuildConfig
import com.vp.movies.data.remote.retrofit.adapter.RxErrorHandlingCallAdapterFactory
import com.vp.movies.data.remote.retrofit.service.MovieService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.omdbapi.com/")
            .client(okHttpClient)
            .build()

    @Provides
    fun providesOkHttp(httpLoggingInterceptor: HttpLoggingInterceptor,
                       @Named("apiKeyInterceptor") apiKeyInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(apiKeyInterceptor)
                .build()
    }

    @Provides
    @Named("apiKeyInterceptor")
    fun providesApiKeyInterceptor(): Interceptor = Interceptor {
        val original = it.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
                .addQueryParameter("apiKey", BuildConfig.API_KEY)
                .build()
        val requestBuilder = original.newBuilder()
                .url(url)

        val request = requestBuilder.build()
        it.proceed(request)
    }

    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun movieService(
            retrofit: Retrofit
    ): MovieService = retrofit.create(MovieService::class.java)
}