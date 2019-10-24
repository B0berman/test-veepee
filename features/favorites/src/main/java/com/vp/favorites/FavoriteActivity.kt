package com.vp.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity() {

    private lateinit var listAdapter: ListAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        initList()

        val favoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)

        favoriteViewModel.fetchMovies()
        favoriteViewModel.favoriteMovies().observe(this, Observer {
            listAdapter.movies = it
        })
    }

    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter.onItemClickListener = {
            openDetails(it.imdbID)
        }
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = listAdapter
    }

    private fun openDetails(imdbID: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
        intent.setPackage(packageName)
        startActivity(intent)
    }
}
