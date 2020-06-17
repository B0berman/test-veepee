package com.vp.movies.di

import android.app.Application
import com.vp.movies.MoviesApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(val application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return application
    }

}