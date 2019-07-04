package com.vp.favorites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.os.Bundle;


import com.vp.detail.model.MovieDetail;
import com.vp.detail.utils.SharedPreference;
import com.vp.favorites.databinding.ActivityFavoriteBinding;

import java.util.ArrayList;
import java.util.List;


public class FavoriteActivity extends AppCompatActivity {

    private SharedPreference sharedPreference;
    private Adapter adapterMovie;
    private ActivityFavoriteBinding binding;
    public List<MovieDetail> data = new ArrayList<>();
    private FavoriteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);
        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        binding.setLifecycleOwner(this);

        sharedPreference = new SharedPreference(this);

        initRecyclerView();

        viewModel.getLiveData().observe(this, new Observer<List<MovieDetail>>() {
            @Override
            public void onChanged(List<MovieDetail> movieDetails) {

                adapterMovie.changeAdapter(movieDetails);

            }
        });
    }


    private void initRecyclerView() {
        adapterMovie = new Adapter(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.list.setLayoutManager(mLayoutManager);
        binding.list.setHasFixedSize(true);
        binding.list.setAdapter(adapterMovie);

    }


}
