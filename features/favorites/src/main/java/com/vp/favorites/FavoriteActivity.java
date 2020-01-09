package com.vp.favorites;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vp.favorites.model.ListItem;
import com.vp.favorites.viewmodel.FavoriteViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class FavoriteActivity extends DaggerAppCompatActivity implements ListAdapter.OnItemClickListener {
    public static final String TAG = "FavoriteActivity";

    @Inject
    ViewModelProvider.Factory factory;

    FavoriteViewModel favoriteViewModel;

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        favoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel.class);

        recyclerView = findViewById(R.id.recyclerView);
        initList();

        favoriteViewModel.observeMovies().observe(this, moviesList -> {
            if (moviesList != null) {
                updateList(listAdapter, moviesList);
            }
        });

        favoriteViewModel.fetchFavoritesMovieList(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Do not forget to update the view if we came back from detail view
        favoriteViewModel.fetchFavoritesMovieList(this);
    }

    private void initList() {
        listAdapter = new ListAdapter();
        listAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void updateList(@NonNull ListAdapter listAdapter, @NonNull List<ListItem> list) {
        if (list.size() != 0) {
            listAdapter.setItems(list);
        }
    }

    @Override
    public void onItemClick(String imdbID) {
        // Create the intent with deep links and add data as Query parameters
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("app://movies/detail?imdbID=" + imdbID));

        startActivity(intent);
    }
}
