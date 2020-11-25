package com.vp.database.di

import android.app.Application
import com.vp.database.db.MovieDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(application: Application)
            = MovieDatabase.getDatabase(application)
}