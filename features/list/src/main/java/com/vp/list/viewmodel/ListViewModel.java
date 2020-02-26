package com.vp.list.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {
    private MutableLiveData<SearchResult> liveData = new MutableLiveData<>();
    private SearchService searchService;

    private String currentTitle = "";

    @Inject
    ListViewModel(@NonNull SearchService searchService) {
        this.searchService = searchService;
    }

    public LiveData<SearchResult> observeMovies() {
        return liveData;
    }

    public void searchMoviesByTitle(@NonNull String title, int page) {

        if (page == 1 && !title.equals(currentTitle)) {
            currentTitle = title;
            liveData.setValue(SearchResult.inProgress());
        }
        searchService.search(title, page).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {

                SearchResponse result = response.body();

                if (result != null) {
                    liveData.setValue(SearchResult.success(response.body().getSearch(), result.getTotalResults()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                liveData.setValue(SearchResult.error());
            }
        });
    }
}
