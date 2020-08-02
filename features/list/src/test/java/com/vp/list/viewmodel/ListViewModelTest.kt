package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.whenever
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import retrofit2.mock.Calls
import java.io.IOException

class ListViewModelTest {
    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @Test
    fun shouldReturnErrorState() {
        //given
        val searchService = Mockito.mock(SearchService::class.java)
        whenever(
                searchService.search(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyInt())).thenReturn(Calls.failure(IOException())
        )
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        assertThat(listViewModel.observeMovies().value!!.listState).isEqualTo(ListState.ERROR)
    }

    @Test
    fun shouldReturnInProgressState() {
        //given
        val searchService = Mockito.mock(SearchService::class.java)
        whenever(
                searchService.search(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyInt())).thenReturn(Calls.response(Mockito.mock(SearchResponse::class.java)
        ))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = Mockito.mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        Mockito.verify(mockObserver).onChanged(SearchResult.inProgress())
    }

    @Test
    fun shouldReturnLoadedState() {
        //given
        val searchService = Mockito.mock(SearchService::class.java)
        whenever(
                searchService.search(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyInt())).thenReturn(Calls.response(Mockito.mock(SearchResponse::class.java)
        ))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = Mockito.mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        assertThat(listViewModel.observeMovies().value!!.listState).isEqualTo(ListState.LOADED)
    }
}