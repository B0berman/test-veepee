package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.model.ListItem
import com.vp.favorites.viewmodel.FavoritesViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoritesActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        initList()

        favoritesViewModel = ViewModelProviders.of(this, factory).get(FavoritesViewModel::class.java)
        favoritesViewModel.favorites().observe(this, Observer {
            updateList(it)
        })
        favoritesViewModel.fetchFavorites()
    }

    private fun initList() {
        recyclerView.adapter = ListAdapter(emptyList(), ::onItemClick)
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView.layoutManager = layoutManager
    }

    private fun onItemClick(itemId: String) {
        val url = "app://movies/detail?imdbID=$itemId"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        favoritesViewModel.fetchFavorites()
    }

    private fun updateList(items: List<ListItem>) {
        recyclerView.adapter = ListAdapter(items, ::onItemClick)
    }
}