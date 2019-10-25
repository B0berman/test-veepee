package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer

import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService

import org.junit.Rule
import org.junit.Test

import java.io.IOException

import retrofit2.mock.Calls

import org.assertj.core.api.Assertions.assertThat
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import retrofit2.Call

class ListViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @Test
    fun shouldReturnErrorState() {
        //given
        val searchService = mock(SearchService::class.java)
        `when`<Call<SearchResponse>>(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure<SearchResponse>(IOException()))
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        assertThat(listViewModel.observeMovies().value?.listState).isEqualTo(ListState.ERROR)
    }

    @Test
    fun shouldReturnInProgressState() {
        val testResponse = SearchResponse()
        //given
        val searchService = mock(SearchService::class.java)
        `when`<Call<SearchResponse>>(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(testResponse))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        verify(mockObserver).onChanged(SearchResult.inProgress())
    }

}