package com.vp.movies.di

import android.app.Application
import androidx.room.Room
import com.vp.db.MovieDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @JvmStatic
    @Provides
    @Singleton
    internal fun movieDatabase(application: Application): MovieDatabase {
        return Room.databaseBuilder(application, MovieDatabase::class.java, "movie_database.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}