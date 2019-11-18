package com.vp.detail.di

import com.vp.movie.abstraction.repositories.FavouriteMovieRepository
import com.vp.movie.abstraction.usecases.FavouriteMovieUseCase
import com.vp.movie.domain.usecases.FavouriteMovieUseCaseImp
import dagger.Module
import dagger.Provides

@Module
class DetailPersistenceModule {

    @Provides
    fun providesFavouriteMoviesUseCase(favouriteMovieRepository: FavouriteMovieRepository): FavouriteMovieUseCase =
            FavouriteMovieUseCaseImp(favouriteMovieRepository)
}