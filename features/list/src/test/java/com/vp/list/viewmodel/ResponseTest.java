package com.vp.list.viewmodel;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import retrofit2.mock.Calls;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ResponseTest implements LifecycleOwner {
    private String currentQuery = "Interview";

    @Rule
    public InstantTaskExecutorRule instantTaskRule = new InstantTaskExecutorRule();

    @Test
    public void shouldReturnResponse() {
        //given
        SearchService searchService = mock(SearchService.class);
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock(SearchResponse.class)));
        ListViewModel listViewModel = new ListViewModel(searchService);
        Observer<SearchResult> mockObserver = (Observer<SearchResult>) mock(Observer.class);
        listViewModel.observeMovies().observeForever(mockObserver);

        listViewModel.observeMovies().observe(this, searchResult -> {
            System.out.println("ListFragment onViewCreated listViewModel.observeMovies()");
            if (searchResult != null) {
                assertThat(searchResult.getItems().size() >0);
                //then
                verify(mockObserver).onChanged(SearchResult.success(searchResult.getItems(), searchResult.getItems().size()));
            }else{
                //then
                assertThat(searchResult != null);
            }
        });
        //when
        listViewModel.searchMoviesByTitle(currentQuery, 1);


    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return new Lifecycle() {
            @Override
            public void addObserver(@NonNull LifecycleObserver observer) {

            }

            @Override
            public void removeObserver(@NonNull LifecycleObserver observer) {

            }

            @NonNull
            @Override
            public State getCurrentState() {
                return State.STARTED;
            }
        };
    }
}