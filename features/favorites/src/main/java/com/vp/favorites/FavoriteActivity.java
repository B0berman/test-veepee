package com.vp.favorites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vp.detail.model.MovieDetail;
import com.vp.detail.model.MovieDetailRealm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class FavoriteActivity extends AppCompatActivity {

    private TextView textEmptyList;
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private ArrayList<MovieDetailRealm> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        recyclerView = findViewById(R.id.recyclerView);
        textEmptyList = findViewById(R.id.textEmptyList);

        listItems = getFavoritesMovies();

        if (listItems.size() > 0) {
            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),
                    getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new FavoritesAdapter(getApplicationContext(), listItems);
            recyclerView.setAdapter(adapter);
        }
        else {
            textEmptyList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    private ArrayList<MovieDetailRealm> getFavoritesMovies() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MovieDetailRealm> realmList = realm.where(MovieDetailRealm.class).findAll();
        ArrayList<MovieDetailRealm> favoritesList = new ArrayList<>();
        favoritesList.addAll(realmList);
        return favoritesList;
    }
}
