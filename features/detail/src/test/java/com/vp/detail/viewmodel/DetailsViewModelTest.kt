package com.vp.detail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import org.junit.Rule
import org.junit.Test
import retrofit2.mock.Calls
import java.io.IOException

internal class DetailsViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    val detailService: DetailService = mock()
    val movieDetail: MovieDetail = mock()

    @Test
    fun `should publish success results`() {
        //given
        val movieId = "id"
        val title = "title"
        whenever(detailService.getMovie(movieId)).thenReturn(Calls.response(movieDetail))

        whenever(movieDetail.title).thenReturn(title)

        val viewModel = DetailsViewModel(detailService)

        val stateObserver = viewModel.state().test()
        val titleObserver = viewModel.title().test()
        val detailsObserver = viewModel.details().test()

        //when
        viewModel.fetchDetails(movieId)

        //then
        stateObserver.assertValueHistory(
                DetailsViewModel.LoadingState.IN_PROGRESS,
                DetailsViewModel.LoadingState.LOADED
        )
        titleObserver.assertValue(title)
        detailsObserver.assertValue(movieDetail)
    }

    @Test
    fun `should publish failure results`() {
        //given
        val movieId = "id"
        val error = IOException()
        whenever(detailService.getMovie(movieId)).thenReturn(Calls.failure(error))

        val viewModel = DetailsViewModel(detailService)
        val stateObserver = viewModel.state().test()
        val titleObserver = viewModel.title().test()
        val detailsObserver = viewModel.details().test()

        //when
        viewModel.fetchDetails(movieId)

        //then
        stateObserver.assertValueHistory(
                DetailsViewModel.LoadingState.IN_PROGRESS,
                DetailsViewModel.LoadingState.ERROR
        )
        titleObserver.assertNoValue()
        detailsObserver.assertValue(null)
    }
}