package com.vp.movies.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AndroidModule {

    @Provides
    fun provideContext(app: Application): Context = app
}