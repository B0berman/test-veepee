package com.vp.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity(), FavoriteAdapter.OnItemClickListener {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val favoriteAdapter = FavoriteAdapter(this)
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        viewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)

        favoriteList.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.movieList().observe(this, Observer {
            favoriteAdapter.setItems(it)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchMovieList()
    }

    override fun onItemClick(imdbID: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
        intent.setPackage(packageName)
        startActivity(intent)
    }
}