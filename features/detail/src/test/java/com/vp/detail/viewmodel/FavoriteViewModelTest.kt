package com.vp.detail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vp.favorite.service.FavChangeCallback
import com.vp.favorite.service.FavDisposable
import com.vp.favorite.service.FavoritesService
import org.junit.Rule
import org.junit.Test


internal class FavoriteViewModelTest {
    private val service: FavoritesService = mock()
    private val favDisposable: FavDisposable = mock()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `should save as favorite`() {
        //given
        val id = "id"
        whenever(service.listenId(any(), any())).thenAnswer {
            val change = it.arguments[1] as FavChangeCallback
            change(true)
            favDisposable
        }
        val viewModel = FavoriteViewModel(service)
        val testObserver = viewModel.viewState(id).test()

        //when
        viewModel.onAction(Action.Favorite(id))

        //then
        testObserver.assertValueHistory(ViewState.IsFavorite(true))
        verify(service).save(id, true)
    }

    @Test
    fun `should remove favorite`() {
        //given
        val id = "id"
        whenever(service.listenId(any(), any())).thenAnswer {
            val change = it.arguments[1] as FavChangeCallback
            change(false)
            favDisposable
        }
        val viewModel = FavoriteViewModel(service)
        val testObserver = viewModel.viewState(id).test()

        //when
        viewModel.onAction(Action.RemoveFavorite(id))

        //then
        testObserver.assertValueHistory(ViewState.IsFavorite(false))
        verify(service).save(id, false)
    }
}