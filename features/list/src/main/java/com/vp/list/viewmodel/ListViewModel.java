package com.vp.list.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.vp.list.model.ListItem;
import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel implements SwipeRefreshLayout.OnRefreshListener {
    private MutableLiveData<SearchResult> liveData = new MutableLiveData<>();
    private SearchService searchService;

    private String currentTitle = "";
    private List<ListItem> aggregatedItems = new ArrayList<>();

    @Inject
    ListViewModel(@NonNull SearchService searchService) {
        this.searchService = searchService;
    }

    public LiveData<SearchResult> observeMovies() {
        return liveData;
    }

    public void searchMoviesByTitle(@NonNull String title, int page) {

        if (page == 1 && !title.equals(currentTitle)) {
            updateData(title);
        }
        launchSearchRequest(title, page);
    }

    private void launchSearchRequest(@NonNull String title, int page) {
        searchService.search(title, page).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {

                SearchResponse result = response.body();

                if (result != null) {
                    aggregatedItems.addAll(result.getSearch());
                    liveData.setValue(SearchResult.success(aggregatedItems,aggregatedItems.size()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                liveData.setValue(SearchResult.error());
            }
        });
    }

    private void updateData(@NonNull String title) {
        aggregatedItems.clear();
        currentTitle = title;
        liveData.setValue(SearchResult.inProgress());
    }

    private void refreshData() {
        updateData(currentTitle);
        launchSearchRequest(currentTitle,1);
    }

    @Override
    public void onRefresh() {
        refreshData();
    }
}
