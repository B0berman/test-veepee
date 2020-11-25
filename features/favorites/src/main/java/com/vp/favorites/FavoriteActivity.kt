package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.database.db.MovieDatabase
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity(), FavoriteListAdapter.OnItemClickListener {
    private var listAdapter = FavoriteListAdapter()
    private var favoriteViewModel: FavoriteViewModel? = null

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        favoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)
        favoriteViewModel.let {viewModel ->
            viewModel!!.fetchFavorites()
            viewModel.observeMovies().observe(this, Observer {
                listAdapter.setItems(it)
            })
        }
        initList()
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView.adapter = null
    }

    private fun initList() {
        listAdapter.setOnItemClickListener(this)
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView.layoutManager = layoutManager
        refreshFavorites()
    }

    private fun refreshFavorites() {
        favoriteViewModel!!.fetchFavorites()
    }

    override fun onItemClick(imdbID: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        refreshFavorites()
    }
}