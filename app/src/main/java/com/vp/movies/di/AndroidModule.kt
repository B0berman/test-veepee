package com.vp.movies.di

import android.content.Context
import com.vp.movies.MoviesApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val application: MoviesApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context = application
}