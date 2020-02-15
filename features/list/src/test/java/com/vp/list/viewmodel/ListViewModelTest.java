package com.vp.list.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.List;

import retrofit2.mock.Calls;

import static kotlin.text.Typography.times;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskRule = new InstantTaskExecutorRule();

    @Test
    public void shouldReturnErrorState() {
        //given
        SearchService searchService = mock(SearchService.class);
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure(new IOException()));
        ListViewModel listViewModel = new ListViewModel(searchService);

        //when
        listViewModel.searchMoviesByTitle("title", 1);

        //then
        assertThat(listViewModel.observeMovies().getValue().getListState()).isEqualTo(ListState.ERROR);
    }

    @Test
    public void shouldReturnInProgressState() {
        //given
        SearchService searchService = mock(SearchService.class);
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock(SearchResponse.class)));
        ListViewModel listViewModel = new ListViewModel(searchService);
        Observer<SearchResult> mockObserver = (Observer<SearchResult>) mock(Observer.class);
        listViewModel.observeMovies().observeForever(mockObserver);

        //when
        listViewModel.searchMoviesByTitle("title", 1);

        //then
        verify(mockObserver).onChanged(SearchResult.inProgress());
    }

    @Test
    public void shouldReturnSuccessState() {
        //given
        SearchService searchService = mock(SearchService.class);
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock(SearchResponse.class)));
        ListViewModel listViewModel = new ListViewModel(searchService);

        Observer<SearchResult> mockObserver = (Observer<SearchResult>) mock(Observer.class);
        listViewModel.observeMovies().observeForever(mockObserver);

        //when
        listViewModel.searchMoviesByTitle("title", 1);

        ArgumentCaptor<SearchResult> listStateArgumentCaptor = ArgumentCaptor.forClass(SearchResult.class);
        // state should change 2 times
        // record state change
        verify(mockObserver, times(2)).onChanged(listStateArgumentCaptor.capture());
        assertEquals(2, listStateArgumentCaptor.getAllValues().size());

        List<SearchResult> states = listStateArgumentCaptor.getAllValues();
        // check the expected state change sequence
        assertThat(states.get(0).getListState()).isEqualTo(ListState.IN_PROGRESS);
        assertThat(states.get(1).getListState()).isEqualTo(ListState.LOADED);

        //then check the final state
        assertThat(listViewModel.observeMovies().getValue().getListState()).isEqualTo(ListState.LOADED);
    }

}