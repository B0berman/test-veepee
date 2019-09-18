package com.vp.movies.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Uxio Lorenzo on 2019-09-10.
 */
@Module
object AppModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

}