package com.vp.favorite.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.vp.favorite.service.FavoritesService
import com.vp.favorite.service.SharedPreferencesFavoritesService
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class FavoritesCommonModule {
    @Singleton
    @Provides
    @Named("fav")
    fun provideSharedPref(application: Application): SharedPreferences = application.getSharedPreferences("favorites", MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideFavService(@Named("fav") prefs: SharedPreferences): FavoritesService = SharedPreferencesFavoritesService(prefs)
}