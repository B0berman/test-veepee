package com.vp.favorites.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.vp.CompositeDisposableCalls
import com.vp.detail.model.MovieDetail
import com.vp.favorite.usecase.GetFavorites
import com.vp.favorite.usecase.OnError
import com.vp.favorite.usecase.OnSuccess
import org.junit.Rule
import org.junit.Test
import java.io.IOException


internal class FavoriteViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    val getFavorites: GetFavorites = mock()
    val movieDetail: MovieDetail = mock()
    val disposableCalls: CompositeDisposableCalls = mock()

    @Test
    fun `should load favorites contents successfully`() {
        //given
        val viewModel = FavoriteViewModel(getFavorites)
        val testObservable = viewModel.viewState.test()
        whenever(getFavorites.execute(any(), any())).thenAnswer {
            val onSuccess = it.arguments[0] as OnSuccess
            val onError = it.arguments[0] as OnError
            onSuccess(listOf(movieDetail))
            disposableCalls
        }

        //when
        viewModel.onAction(Action.Load)

        //then
        testObservable.assertValueHistory(
                ViewState.Loading,
                ViewState.Content(listOf(movieDetail))
        )
    }

    @Test
    fun `should load favorites empty state successfully`() {
        //given
        val viewModel = FavoriteViewModel(getFavorites)
        val testObservable = viewModel.viewState.test()
        whenever(getFavorites.execute(any(), any())).thenAnswer {
            val onSuccess = it.arguments[0] as OnSuccess
            onSuccess(emptyList())
            disposableCalls
        }

        //when
        viewModel.onAction(Action.Load)

        //then
        testObservable.assertValueHistory(
                ViewState.Loading,
                ViewState.Empty
        )
    }

    @Test
    fun `should fail to load favorites contents`() {
        //given
        val error = IOException()
        val viewModel = FavoriteViewModel(getFavorites)
        val testObservable = viewModel.viewState.test()
        whenever(getFavorites.execute(any(), any())).thenAnswer {

            val onError = it.arguments[1] as OnError
            onError(error)
            disposableCalls
        }

        //when
        viewModel.onAction(Action.Load)

        //then
        testObservable.assertValueHistory(
                ViewState.Loading,
                ViewState.Failure
        )
    }

    @Test
    fun `should try again load favorites contents successfully`() {
        //given
        val viewModel = FavoriteViewModel(getFavorites)
        val testObservable = viewModel.viewState.test()
        whenever(getFavorites.execute(any(), any())).thenAnswer {
            val onSuccess = it.arguments[0] as OnSuccess
            onSuccess(listOf(movieDetail))
            disposableCalls
        }

        //when
        viewModel.onAction(Action.TryAgain)

        //then
        testObservable.assertValueHistory(
                ViewState.Loading,
                ViewState.Content(listOf(movieDetail))
        )
    }

    @Test
    fun `should try again load favorites empty state successfully`() {
        //given
        val viewModel = FavoriteViewModel(getFavorites)
        val testObservable = viewModel.viewState.test()
        whenever(getFavorites.execute(any(), any())).thenAnswer {
            val onSuccess = it.arguments[0] as OnSuccess
            onSuccess(emptyList())
            disposableCalls
        }

        //when
        viewModel.onAction(Action.TryAgain)

        //then
        testObservable.assertValueHistory(
                ViewState.Loading,
                ViewState.Empty
        )
    }

    @Test
    fun `should fail to try again load favorites contents`() {
        //given
        val error = IOException()
        val viewModel = FavoriteViewModel(getFavorites)
        val testObservable = viewModel.viewState.test()
        whenever(getFavorites.execute(any(), any())).thenAnswer {

            val onError = it.arguments[1] as OnError
            onError(error)
            disposableCalls
        }

        //when
        viewModel.onAction(Action.TryAgain)

        //then
        testObservable.assertValueHistory(
                ViewState.Loading,
                ViewState.Failure
        )
    }
}