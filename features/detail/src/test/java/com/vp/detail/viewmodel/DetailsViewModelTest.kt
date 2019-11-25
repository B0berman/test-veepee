package com.vp.detail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.vp.detail.model.Events
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.favorites.core.FavoriteInteractor
import com.vp.favorites.core.FavoriteRepository
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import retrofit2.Response
import retrofit2.mock.Calls
import java.io.IOException

class DetailsViewModelTest {

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()


    @Test
    fun shouldReturnErrorState() {

        val mockedDetailsService = mock(DetailService::class.java)
        val mockedFavoriteRepository = mock(FavoriteRepository::class.java)

        Mockito.`when`(mockedDetailsService.getMovie(Mockito.anyString())).thenReturn(Calls.failure(IOException()))

        val detailsViewModel = DetailsViewModel(mockedDetailsService, FavoriteInteractor(mockedFavoriteRepository))

        val mockedObserver : Observer<DetailsViewModel.LoadingState> = mock(Observer::class.java) as Observer<DetailsViewModel.LoadingState>
        detailsViewModel.state().observeForever(mockedObserver)

        detailsViewModel.sendEvent(Events.GetMovieDetails("1234"))

        verify(mockedObserver).onChanged(DetailsViewModel.LoadingState.IN_PROGRESS)
        verify(mockedObserver).onChanged(DetailsViewModel.LoadingState.ERROR)

    }

    @Test
    fun shouldReturnContent() {
        val mockedDetailsService = mock(DetailService::class.java)
        val mockedFavoriteRepository = mock(FavoriteRepository::class.java)

        val movieDetail = MovieDetail("1234","Title", "2019", "1", "director", "plot", "poster")

        val movieDetailResponse = Response.success(movieDetail)

        Mockito.`when`(mockedDetailsService.getMovie(Mockito.anyString())).thenReturn(Calls.response(movieDetailResponse))
        Mockito.`when`(mockedFavoriteRepository.isMovieInFavorite(Mockito.anyString())).thenReturn(Single.just(true))

        val detailsViewModel = DetailsViewModel(mockedDetailsService, FavoriteInteractor(mockedFavoriteRepository))

        val mockedObserver : Observer<MovieDetail> = mock(Observer::class.java) as Observer<MovieDetail>
        detailsViewModel.details.observeForever(mockedObserver)

        detailsViewModel.sendEvent(Events.GetMovieDetails("tt2061712"))

        assertThat(detailsViewModel.details.value).isNotNull()

        System.out.println(detailsViewModel.details.value)
    }

}