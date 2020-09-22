package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import com.vp.list.viewmodel.ListState.ERROR
import com.vp.list.viewmodel.SearchResult.Companion.inProgress
import com.vp.list.viewmodel.SearchResult.Companion.success
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import retrofit2.mock.Calls
import retrofit2.mock.Calls.response
import java.io.IOException

class ListViewModelTest {
  @get:Rule
  var instantTaskRule = InstantTaskExecutorRule()

  @Test fun shouldReturnErrorState() {
    //given
    val searchService = mock(SearchService::class.java)
    `when`(searchService.search(anyString(), anyInt())).thenReturn(
        Calls.failure(IOException())
    )
    val listViewModel = ListViewModel(searchService)
    //when
    listViewModel.searchMoviesByTitle("title", 1)
    //then
    assertThat(listViewModel.observeMovies().value!!.listState).isEqualTo(ERROR)
  }

  @Test fun shouldReturnInProgressState() {
    //given
    val searchService = mock(SearchService::class.java)
    `when`(searchService.search(anyString(), anyInt())).thenReturn(response(mock(SearchResponse::class.java)))
    val listViewModel = ListViewModel(searchService)
    val mockObserver = mock(Observer::class.java) as Observer<SearchResult>
    listViewModel.observeMovies().observeForever(mockObserver)
    //when
    listViewModel.searchMoviesByTitle("title", 1)
    //then
    Mockito.verify(mockObserver).onChanged(inProgress())
  }

  @Test fun shouldReturnOnSuccessState() {
    //given
    val searchService = mock(SearchService::class.java)
    val searchResponse = mock(SearchResponse::class.java)

    `when`(searchResponse.hasResponse()).thenReturn(true)
    `when`(searchService.search(anyString(), anyInt())).thenReturn(response(searchResponse))
    val listViewModel = ListViewModel(searchService)
    val mockObserver = mock(Observer::class.java) as Observer<SearchResult>
    listViewModel.observeMovies().observeForever(mockObserver)
    //when
    listViewModel.searchMoviesByTitle("title", 1)
    //then
    ArgumentCaptor.forClass(SearchResult::class.java).apply {
      verify(mockObserver, times(2)).onChanged(capture())
      assertThat(allValues[0] == inProgress())
      assertThat(allValues[1] == success(allValues[1].items, allValues[1].totalResult))
    }
  }
}