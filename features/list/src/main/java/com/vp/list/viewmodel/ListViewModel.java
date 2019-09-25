package com.vp.list.viewmodel;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.vp.list.ListAdapter;
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
    private MutableLiveData<SearchResult> liveData = new MutableLiveData<>();
    private SearchService searchService;

    private String currentTitle = "";
    private List<ListItem> aggregatedItems = new ArrayList<>();
    ListAdapter listAdapter;
    @Inject
    ListViewModel(@NonNull SearchService searchService) {
        this.searchService = searchService;
    }

    public ArrayList<ListItem> getFavourites(SharedPreferences pref){
        ArrayList<ListItem> out = new ArrayList<>();
        for(ListItem item : aggregatedItems){
            String s = pref.getString(item.getID(), "");
            String it = item.getID();
            if(pref.getString(item.getID(), "") != ""){
                out.add(item);

            }
        }
        return out;
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
                System.out.println("ListViewModel SearchResponse");

                for(ListItem item : result.getSearch()){
                    System.out.println(item.getID());
                    System.out.println(item.getTitle());
                    System.out.println(item.getYear());
                    System.out.println(item.getPoster());
                }
                if (result != null) {
                    aggregatedItems.addAll(result.getSearch());
                  //  listAdapter.setItems(aggregatedItems);
                  // listAdapter.notifyDataSetChanged();
                    liveData.setValue(SearchResult.success(result.getSearch(), result.getSearch().size()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                liveData.setValue(SearchResult.error());
            }
        });
    }

    //public void setAdapter(ListAdapter listAdapterin) {
    //    listAdapter = listAdapterin;
    //}
}
