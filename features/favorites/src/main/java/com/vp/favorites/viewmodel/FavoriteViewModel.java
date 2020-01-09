package com.vp.favorites.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vp.common.utils.StorageUtil;
import com.vp.favorites.model.FetchResponse;
import com.vp.favorites.model.ListItem;
import com.vp.favorites.service.FetchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteViewModel extends ViewModel {
    private FetchService fetchService;

    private MutableLiveData<List<ListItem>> movieListliveData = new MutableLiveData<>();

    public MutableLiveData<List<ListItem>> observeMovies() {
        return movieListliveData;
    }

    @Inject
    public FavoriteViewModel(@NonNull FetchService fetchService) {
        this.fetchService = fetchService;
    }

    public void fetchFavoritesMovieList(Context context) {
        Set<String> set = StorageUtil.Companion.getFavoriteMoviesList(context);

        List<String> moviesIds = new ArrayList<>(set);
        fetchFavoritesMoviesData(moviesIds);
    }


    private void fetchFavoritesMoviesData(List<String> moviesIds) {
        HashMap<String, ListItem> mapMovies = new HashMap<>();

        // browser each movie reference
        for (String movieId : moviesIds) {
            ListItem movie = new ListItem();

            // Set the status as Loading until the service call ends
            movie.setStatus(ListItem.Status.LOADING);
            movie.setImdbID(movieId);
            mapMovies.put(movieId, movie);
            fetchService.search(movieId).enqueue(new Callback<FetchResponse>() {
                @Override
                public void onResponse(Call<FetchResponse> call, Response<FetchResponse> response) {
                    mapMovies.get(movieId).setStatus(ListItem.Status.LOADED);
                    mapMovies.get(movieId).setTitle(response.body().getTitle());
                    mapMovies.get(movieId).setPoster(response.body().getPoster());
                    //Replace the livedata to inform the view
                    movieListliveData.postValue(new ArrayList<>(mapMovies.values()));
                }

                @Override
                public void onFailure(Call<FetchResponse> call, Throwable t) {
                    mapMovies.get(movieId).setStatus(ListItem.Status.ERROR);

                    //Replace the livedata to inform the view
                    movieListliveData.postValue(new ArrayList<>(mapMovies.values()));
                }
            });
        }

        //Replace the livedata to inform the view
        movieListliveData.postValue(new ArrayList<>(mapMovies.values()));
    }
}
