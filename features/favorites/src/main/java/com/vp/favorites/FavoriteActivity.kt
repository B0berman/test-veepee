package com.vp.favorites;

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class FavoriteActivity : DaggerAppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: FavoriteMoviesAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        recyclerView = findViewById(R.id.favorite_recyclerView)
        adapter = FavoriteMoviesAdapter()
        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)
        viewModel.favoriteMovies().observe(this, Observer { favoriteMovies ->
            adapter.submitList(favoriteMovies)
        })
        viewModel.getFavoriteMovies()
    }

}
