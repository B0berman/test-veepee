package com.vp.movies.di

import android.app.Application
import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.vp.movies.db.MovieDB
import com.vp.movies.db.MovieDatabase
import dagger.Module
import javax.inject.Inject
import dagger.Provides
import javax.inject.Singleton



@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesMovieDatabase(db: MovieDB) : MovieDatabase {
        return MovieDatabase(db)
    }

    @Singleton
    @Provides
    fun providesSqlDriver(application: Application): SqlDriver {
        return AndroidSqliteDriver(MovieDB.Schema, application, "Movie.db")
    }

    @Singleton
    @Provides
    fun providesMovieDB(sqlDriver: SqlDriver) : MovieDB {
        return MovieDB(sqlDriver)
    }
}