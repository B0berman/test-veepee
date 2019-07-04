package com.vp.detail.di

import com.vp.core.favorites.FavoriteMoviesRepository
import com.vp.detail.service.DetailService
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.Module
import dagger.Provides

@Module
class DetailViewModelsModule {

    @Provides
    fun provideDetailsViewModelDependencies(
            detailService: DetailService,
            favoriteRepository: FavoriteMoviesRepository
    ) = DetailsViewModel.Dependencies(detailService, favoriteRepository)
}