package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.verify
import retrofit2.mock.Calls
import java.io.IOException

class ListViewModelTest {

    val instantTaskRule = InstantTaskExecutorRule()
        @Rule get

    @Test
    fun shouldReturnErrorState() {
        //given
        val searchService = mock<SearchService>()
        whenever(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure<SearchResponse>(IOException()))
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        assertThat(listViewModel.movies.value!!.listState).isEqualTo(ListState.ERROR)
    }

    @Test
    fun shouldReturnInProgressState() {
        //given
        val searchService = mock<SearchService>()
        whenever(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock<SearchResponse>()))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = mock<Observer<SearchResult>>()
        listViewModel.movies.observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        verify(mockObserver).onChanged(SearchResult.inProgress())
    }

    @Test
    fun shouldReturnInSuccessState() {
        //given
        val response = mock<SearchResponse>()
        val list = mutableListOf<ListItem>(mock(), mock(), mock())
        whenever(response.search).thenReturn(list)
        whenever(response.totalResults).thenReturn(3)

        val searchService = mock<SearchService>()
        whenever(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(response))
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        assertThat(listViewModel.movies.value!!.listState).isEqualTo(ListState.LOADED)
        assertThat(listViewModel.movies.value!!.totalResult).isEqualTo(3)
        assertThat(listViewModel.movies.value!!.items).isEqualTo(list)
    }

}