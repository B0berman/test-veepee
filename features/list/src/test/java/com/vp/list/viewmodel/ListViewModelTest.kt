package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import retrofit2.mock.Calls
import java.io.IOException

class ListViewModelTest {
    @get:Rule
    var instantTaskRule = InstantTaskExecutorRule()

    @Test
    fun shouldReturnErrorState() {
        //given
        val searchService = Mockito.mock(SearchService::class.java)
        Mockito.`when`(
                searchService.search(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
        ).thenReturn(Calls.failure(IOException()))
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        Assertions.assertThat(listViewModel.observeMovies().value?.listState)
                .isEqualTo(ListState.ERROR)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun shouldReturnInProgressState() {
        //given
        val searchService = Mockito.mock(SearchService::class.java)
        Mockito.`when`(
                searchService.search(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
        ).thenReturn(Calls.response(Mockito.mock(SearchResponse::class.java)))

        val listViewModel = ListViewModel(searchService)
        val mockObserver = Mockito.mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        verify(mockObserver)
                .onChanged(SearchResult.inProgress())
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun shouldReturnSuccessState() {
        //given
        val searchService = Mockito.mock(SearchService::class.java)

        val listItem = listOf(Mockito.mock(ListItem::class.java))
        val totalResults = 2
        val response = Mockito.mock(SearchResponse::class.java)

        Mockito.`when`(response.search)
                .thenReturn(listItem)

        Mockito.`when`(response.totalResults)
                .thenReturn(totalResults)

        Mockito.`when`(
                searchService.search(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
        ).thenReturn(Calls.response(response))

        val listViewModel = ListViewModel(searchService)
        val mockObserver = Mockito.mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        verify(mockObserver)
                .onChanged(SearchResult.inProgress())

        val captor = ArgumentCaptor.forClass(SearchResult::class.java)
        captor.run {
            verify(mockObserver, times(2)).onChanged(capture())
            Assert.assertEquals(
                    SearchResult.success(
                            listItem,
                            totalResults
                    ),
                    value
            )
        }
    }
}
