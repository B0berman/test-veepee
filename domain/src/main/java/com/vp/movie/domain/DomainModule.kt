package com.vp.movie.domain

import com.vp.movie.abstraction.repositories.FavouriteMovieRepository
import com.vp.movie.abstraction.usecases.FavouriteMovieUseCase
import com.vp.movie.domain.ports.persistence.FavouriteMovieDaoPort
import com.vp.movie.domain.repositories.FavouriteMoviePersistenceAdapter
import com.vp.movie.domain.usecases.FavouriteMovieUseCaseImp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun providesFavouriteMovieRepository(favouriteMovieDao: FavouriteMovieDaoPort): FavouriteMovieRepository =
            FavouriteMoviePersistenceAdapter(favouriteMovieDao)

}