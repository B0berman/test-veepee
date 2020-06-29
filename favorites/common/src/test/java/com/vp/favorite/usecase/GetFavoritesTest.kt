package com.vp.favorite.usecase

import com.nhaarman.mockitokotlin2.*
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.favorite.service.FavoritesService

import org.junit.Test
import retrofit2.mock.Calls
import java.io.IOException

class GetFavoritesTest {

    private val favoritesService: FavoritesService = mock()
    private val detailService: DetailService = mock()
    private val onSuccess: OnSuccess = mock()
    private val onError: OnError = mock()
    private val movieDetail: MovieDetail = mock()

    @Test
    fun `should load movie details when there is at least one favorite`() {
        //given
        val data = listOf(movieDetail)
        val id = "id"
        whenever(detailService.getMovie(id)).thenReturn(Calls.response(movieDetail))
        whenever(favoritesService.getAllFavorite()).thenReturn(listOf(id))
        val getFavorites = GetFavorites(detailService, favoritesService)
        //when
        getFavorites.execute(onSuccess, onError)

        //then
        verify(onSuccess).invoke(data)
    }

    @Test
    fun `should return an empty list when there is no favorite movie`() {
        //given
        val data = emptyList<MovieDetail>()
        whenever(favoritesService.getAllFavorite()).thenReturn(emptyList())
        val getFavorites = GetFavorites(detailService, favoritesService)

        //when
        getFavorites.execute(onSuccess, onError)

        //then
        verify(onSuccess).invoke(data)
        verify(detailService, never()).getMovie(any())
    }

    @Test
    fun `should fail to load movie details when an error happens`() {
        //given
        val error = IOException()
        whenever(detailService.getMovie("id")).thenReturn(Calls.response(movieDetail))
        whenever(detailService.getMovie("id1")).thenReturn(Calls.failure(error))
        whenever(favoritesService.getAllFavorite()).thenReturn(listOf("id", "id1"))
        val getFavorites = GetFavorites(detailService, favoritesService)

        //when
        getFavorites.execute(onSuccess, onError)

        //then
        verify(onError).invoke(CompositeExecutionException(listOf(error)))
        verify(onSuccess, never()).invoke(any())
    }
}