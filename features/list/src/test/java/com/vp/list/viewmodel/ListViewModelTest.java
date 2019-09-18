package com.vp.list.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
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
    public void shouldReturnLoadedState() {
        //given
        String jsonResponse = "{\n" +
                "    \"Response\": \"True\",\n" +
                "    \"Search\": [\n" +
                "        {\n" +
                "            \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNDg0MzcwNTczMl5BMl5BanBnXkFtZTcwMDM1NjI4OA@@._V1_SX300.jpg\",\n" +
                "            \"Title\": \"The Interview\",\n" +
                "            \"Type\": \"movie\",\n" +
                "            \"Year\": \"2013\",\n" +
                "            \"imdbID\": \"tt2533492\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNGM2YjE0ZTQtOTA3YS00MDJmLWExM2UtODk3NTgyYzJhYTgwXkEyXkFqcGdeQXVyMzM2OTQwMDY@._V1_SX300.jpg\",\n" +
                "            \"Title\": \"Interview with a Serial Killer\",\n" +
                "            \"Type\": \"movie\",\n" +
                "            \"Year\": \"2014\",\n" +
                "            \"imdbID\": \"tt3421418\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"totalResults\": \"1070\"\n" +
                "}";
        SearchResponse searchResponse = new Gson().fromJson(jsonResponse, SearchResponse.class);

        SearchService searchService = mock(SearchService.class);
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(searchResponse));
        ListViewModel listViewModel = new ListViewModel(searchService);
        Observer<SearchResult> mockObserver = (Observer<SearchResult>) mock(Observer.class);
        listViewModel.observeMovies().observeForever(mockObserver);

        //when
        listViewModel.searchMoviesByTitle("title", 1);

        //then
        SearchResult searchResult = listViewModel.observeMovies().getValue();
        assertThat(searchResult.getItems().size()).isEqualTo(searchResponse.getSearch().size());
        assertThat(searchResult.getItems()).isEqualTo(searchResponse.getSearch());

    }

}