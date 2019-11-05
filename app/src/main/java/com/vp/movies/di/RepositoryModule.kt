package com.vp.movies.di

import com.vp.movies.data.remote.repository.MovieRepository
import com.vp.movies.data.remote.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun movieRepository(repository: MovieRepositoryImpl): MovieRepository
}
