package com.vp.movies.di

import android.app.Application
import androidx.room.Room
import com.vp.movies.persistence.DatabaseMetaData.NAME
import com.vp.movies.persistence.MovieDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Singleton
    @Provides
    fun providesRoom(application: Application) =
            Room.databaseBuilder(
                    application,
                    MovieDatabase::class.java,
                    NAME
            ).build()
}
