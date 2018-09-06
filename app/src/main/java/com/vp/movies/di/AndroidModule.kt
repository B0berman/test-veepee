package com.vp.movies.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class AndroidModule {
    @Binds
    abstract fun bindsContext(application: Application): Context
}