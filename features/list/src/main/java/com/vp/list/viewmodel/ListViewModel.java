package com.vp.list.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vp.CompositeDisposableCalls;
import com.vp.list.model.ListItem;
import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {
    private CompositeDisposableCalls disposables = new CompositeDisposableCalls();

    private MutableLiveData<SearchResult> liveData = new MutableLiveData<>();
    private SearchService searchService;

    private String currentTitle = "";
    private List<ListItem> aggregatedItems = new ArrayList<>();
    private int page = 1;

    @Inject
    ListViewModel(@NonNull SearchService searchService) {
        this.searchService = searchService;
    }

    public LiveData<SearchResult> observeMovies() {
        return liveData;
    }

    public void searchMoviesByTitle(@NonNull String title) {
        currentTitle = title;
        loadFirstPageForCurrentTitle();
    }

    public void loadFirstPageForCurrentTitle() {
        aggregatedItems.clear();
        page = 1;
        loadPage();
    }

    public void loadCurrentPageAndTitle() {
        loadPage();
    }

    public void loadNextPageForCurrentQuery() {
        page += 1;
        loadPage();
    }

    private void loadPage() {
        liveData.setValue(SearchResult.inProgress());
        Call<SearchResponse> call = searchService.search(currentTitle, page);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {

                SearchResponse result = response.body();

                if (result != null) {
                    aggregatedItems.addAll(result.getSearch());
                }
                liveData.setValue(SearchResult.success(aggregatedItems, result.getSearch().size()));
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                liveData.setValue(SearchResult.error());
            }
        });
        disposables.add(call);
    }

    @Override
    protected void onCleared() {
        disposables.cancel();
        super.onCleared();
    }
}
