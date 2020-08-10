package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vp.list.model.ListItem

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
import java.util.*

class ListViewModelTest {
    @get:Rule
    var instantTaskRule = InstantTaskExecutorRule()

    @Test
    fun shouldReturnErrorState() {
        //given
        val searchService = mock(SearchService::class.java)
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure(IOException()))
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        assertThat(listViewModel.observeMovies().value!!.listState).isEqualTo(ListState.ERROR)
    }

    @Test
    fun shouldReturnInProgressState() {
        //given
        val searchService = mock(SearchService::class.java)
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock(SearchResponse::class.java)))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        verify(mockObserver).onChanged(SearchResult.inProgress())
    }

    @Test
    fun shouldReturnSuccessState() {
        //given
        val searchService = mock(SearchService::class.java)
        val searchResponse = mock(SearchResponse::class.java)
        val mockListItem = mock(ListItem::class.java)
        val searchList = LinkedList<ListItem>().apply {
            add(mockListItem)
            add(mockListItem)
            add(mockListItem)
        }
        val mockObserver = mock(Observer::class.java) as Observer<SearchResult>

        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(searchResponse))
        `when`(searchResponse.getSearchList()).thenReturn(searchList)
        `when`(searchResponse.getTotalResult()).thenReturn(3)

        val listViewModel = ListViewModel(searchService)
        listViewModel.observeMovies().observeForever(mockObserver)
        //when
        listViewModel.searchMoviesByTitle("title", 1)

        verify(mockObserver).onChanged(SearchResult.success(searchList, 3))
    }

}