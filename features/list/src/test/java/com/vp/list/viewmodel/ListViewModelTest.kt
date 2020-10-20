package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import com.vp.list.viewmodel.ListState.ERROR
import com.vp.list.viewmodel.SearchResult.Companion.inProgress
import com.vp.list.viewmodel.SearchResult.Companion.success
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import retrofit2.mock.Calls
import java.io.IOException

fun <T> whenever(methodCall: T) = Mockito.`when`(methodCall)

class ListViewModelTest {
    @get:Rule
    var instantTaskRule = InstantTaskExecutorRule()

    @Test
    fun shouldReturnErrorState() {
        //given
        val searchService = mock(SearchService::class.java)
        whenever(searchService.search(anyString(), anyInt()))
            .thenReturn(Calls.failure(IOException()))
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        Assertions.assertThat(listViewModel.movies.value!!.listState).isEqualTo(ERROR)
    }

    @Test fun shouldReturnInProgressState() {
        //given
        val searchService = mock(SearchService::class.java)
        whenever(searchService.search(anyString(), anyInt()))
            .thenReturn(Calls.response(mock(SearchResponse::class.java)))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = mock(
            Observer::class.java
        ) as Observer<SearchResult>
        listViewModel.movies.observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        verify(mockObserver).onChanged(inProgress())
    }

    @Test fun shouldReturnInSuccessState() {
        //given
        val searchService = mock(SearchService::class.java)
        val searchResponse = mock(SearchResponse::class.java)
        whenever(searchService.search(anyString(), anyInt()))
            .thenReturn(Calls.response(searchResponse))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = mock(
            Observer::class.java
        ) as Observer<SearchResult>
        listViewModel.movies.observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        verify(mockObserver).onChanged(inProgress())
        verify(mockObserver)
            .onChanged(success(searchResponse.search, searchResponse.totalResults))
    }
}
