package com.vp.movies.di

import android.app.Application
import android.content.Context
import com.vp.movies.MoviesApplication
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Provides application-wide dependencies.
 */
@Module
abstract class ApplicationModule {

    @Binds
    @Singleton
    internal abstract fun context(application: Application): Context
}
