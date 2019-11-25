package com.vp.list.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.vp.list.BaseTest;
import com.vp.list.model.ListItem;
import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.mock.Calls;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Could subclass BaseTest to run with Robolectric and access injected mock with Dagger, but We let it as it(in isolation) - CE
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
        SearchService searchService = mock(SearchService.class);

        Gson gson = new Gson();

        List<ListItem> itemList = generateListItem(6, gson);
        String jsonResponse = "{ Search:" + gson.toJson(itemList) + ", totalResults:" + itemList.size() + ", response:True }";
        Response<SearchResponse> searchResponseResponse = Response.success(gson.fromJson(jsonResponse, SearchResponse.class));
        Call<SearchResponse> call = Calls.response(searchResponseResponse);

        when(searchService.search(anyString(), anyInt())).thenReturn(call);

        ListViewModel listViewModel = new ListViewModel(searchService);
        Observer<SearchResult> searchResultObserver = mock(Observer.class);
        listViewModel.observeMovies().observeForever(searchResultObserver);

        listViewModel.searchMoviesByTitle("Test Title", 4);

        List<ListItem> expectedItemList = searchResponseResponse.body().getSearch();
        verify(searchResultObserver).onChanged(SearchResult.success(expectedItemList, 6));
    }


    private List<ListItem> generateListItem(int numberOfItem, Gson gson) {
        List<ListItem> itemList = new ArrayList<>();

        for(int i = 0; i < numberOfItem; i++) {
            String listItemJson = "{\"Title\":\"title " + i + "\", \"Years\":\"2019\", \"Poster\":\"poster " + i + "\"}";
            itemList.add(gson.fromJson(listItemJson, ListItem.class));
        }

        return itemList;
    }

}