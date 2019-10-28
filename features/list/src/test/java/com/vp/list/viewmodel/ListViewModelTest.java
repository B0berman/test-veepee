package com.vp.list.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.vp.list.model.ListItem;
import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.mock.Calls;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskRule = new InstantTaskExecutorRule();

    @Mock
    private SearchService searchService;

    @Mock
    private Observer<SearchResult> mockObserver;

    private ListViewModel subject;

    private static final String mockTitle = "title";
    private static final int mockPage = 1;

    @Before
    public void prepare() {
        subject = new ListViewModel(searchService);
    }

    @Test
    public void shouldReturnErrorState() {
        //given
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure(new IOException()));
        subject.observeMovies().observeForever(mockObserver);

        //when
        subject.searchMoviesByTitle(mockTitle, mockPage);

        //then
        verify(mockObserver).onChanged(SearchResult.error());
    }

    @Test
    public void shouldReturnInProgressState() {
        //given
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock(SearchResponse.class)));
        subject.observeMovies().observeForever(mockObserver);

        //when
        subject.searchMoviesByTitle(mockTitle, mockPage);

        //then
        verify(mockObserver).onChanged(SearchResult.inProgress());
    }

    @Test
    public void shouldReturnSuccessState() {
        //given
        SearchResponse response = mock(SearchResponse.class);
        List<ListItem> items = new ArrayList<>();

        when(response.getSearch()).thenReturn(items);
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(response));
        subject.observeMovies().observeForever(mockObserver);

        //when
        subject.searchMoviesByTitle(mockTitle, mockPage);

        //then
        verify(mockObserver).onChanged(SearchResult.success(items, items.size()));
    }

}