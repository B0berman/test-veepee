package com.vp.list.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vp.api.model.FavoritesRepository;
import com.vp.api.model.ListItem;
import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {
    private MutableLiveData<SearchResult> liveData = new MutableLiveData<>();
    private SearchService searchService;
    private FavoritesRepository favoritesRepository;

    private String currentTitle = "";
    private List<ListItem> aggregatedItems = new ArrayList<>();

    @Inject
    ListViewModel(
            @NonNull SearchService searchService,
            @NonNull FavoritesRepository favoritesRepository
    ) {
        this.searchService = searchService;
        this.favoritesRepository = favoritesRepository;
    }

    public LiveData<SearchResult> observeMovies() {
        return liveData;
    }

    public void fetchFavoritesMovies() {
        favoritesRepository.getFavorites().observeForever(favorites ->
                liveData.setValue(SearchResult.success(favorites, favorites.size()))
        );
    }

    public void searchMoviesByTitle(@NonNull String title, int page) {

        if (page == 1 && !title.equals(currentTitle)) {
            aggregatedItems.clear();
            currentTitle = title;
            liveData.setValue(SearchResult.inProgress());
        }
        searchService.search(title, page).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {

                SearchResponse result = response.body();

                if (result != null) {
                    aggregatedItems.addAll(result.getSearch());
                    liveData.setValue(SearchResult.success(aggregatedItems, result.getTotalResults()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                liveData.setValue(SearchResult.error());
            }
        });
    }
}
