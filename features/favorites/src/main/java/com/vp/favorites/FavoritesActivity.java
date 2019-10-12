package com.vp.favorites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vp.favorites.model.MovieDetail;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class FavoritesActivity extends AppCompatActivity implements FavoritesAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<MovieDetail> favoriteMovies;
    private ProgressBar progressBar;
    private TextView tvEmptyMessage;
    private static final String  DETAIL_DEEPLINK = "app://movies/detail?imdbID=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3);
        recyclerView.setLayoutManager(layoutManager);

        showProgressBar();
        fetchFavorites();
    }

    /**
     * Method to get and display all saved favorites
     */
    private void fetchFavorites() {
        favoriteMovies = new ArrayList<>();
        final Realm realm = Realm.getDefaultInstance();
        final RealmResults<MovieDetail> resultMovies = realm.where(MovieDetail.class).findAll();

        if (resultMovies.isEmpty()) {
            showEmptyView();
        } else {
            favoriteMovies.addAll(realm.copyFromRealm(resultMovies));
            setFavorites(favoriteMovies);
        }

        realm.close();
    }

    private void setFavorites(ArrayList<MovieDetail> favoriteMovies) {
        recyclerView.setVisibility(View.VISIBLE);
        final FavoritesAdapter favoritesAdapter = new FavoritesAdapter();
        favoritesAdapter.setOnItemClickListener(this);
        favoritesAdapter.setItems(favoriteMovies);
        recyclerView.setAdapter(favoritesAdapter);
        hideProgressBar();
    }

    private void showEmptyView() {
        hideProgressBar();
        tvEmptyMessage.setVisibility(View.VISIBLE);
        tvEmptyMessage.setText(getResources().getString(R.string.no_data));
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(String imdbID) {
        makeDetailIntent(imdbID);
    }

    /**
     * Displays the detail activity.
     * @param imdbID the ID of the movie to display.
     */
    private void makeDetailIntent(String imdbID) {
        String deeplink = DETAIL_DEEPLINK + imdbID;
        Intent intent = new Intent (Intent.ACTION_VIEW);
        intent.setData(Uri.parse(deeplink));
        startActivity(intent);
    }
}
