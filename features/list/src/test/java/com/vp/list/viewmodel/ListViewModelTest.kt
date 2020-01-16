package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.mock.Calls
import java.io.IOException

class ListViewModelTest {
    @Rule
    @JvmField
    var instantTaskRule: InstantTaskExecutorRule? = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }


    @Test
    fun shouldReturnErrorState() {
        //given
        val searchService = Mockito.mock(SearchService::class.java)
        Mockito.`when`(
            searchService.search(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(Calls.failure(IOException()))
        val listViewModel =
            ListViewModel(searchService)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        assertThat(listViewModel.observeMovies().value?.listState)
            .isEqualTo(ListState.ERROR)
    }

    @Test
    fun shouldReturnInProgressState() {
        //given
        val searchService = Mockito.mock(SearchService::class.java)
        Mockito.`when`(
            searchService.search(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(
            Calls.response(
                SearchResponse(listOf(), 0)
            )
        )
        val listViewModel =
            ListViewModel(searchService)
        @Suppress("UNCHECKED_CAST")
        val mockObserver =
            Mockito.mock(
                Observer::class.java
            ) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        Mockito.verify(
            mockObserver
        ).onChanged(SearchResult.inProgress())
    }

    @Test
    fun shouldReturnSucessState() {
        //given
        val searchService = Mockito.mock(SearchService::class.java)
        Mockito.`when`(
            searchService.search(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(Calls.response(SearchResponse(listOf(), 0)))
        val listViewModel =
            ListViewModel(searchService)
        @Suppress("UNCHECKED_CAST")
        val mockObserver =
            Mockito.mock(
                Observer::class.java
            ) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        val result = listViewModel.observeMovies().value?.listState
        assertThat(result).isEqualTo(ListState.LOADED)
    }
}