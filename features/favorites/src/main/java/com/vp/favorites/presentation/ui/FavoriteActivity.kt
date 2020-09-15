package com.vp.favorites.presentation.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vp.favorites.R.layout
import com.vp.favorites.domain.model.FavoriteItem
import com.vp.favorites.presentation.ui.adapters.FavoriteAdapter
import com.vp.favorites.presentation.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.recyclerView
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity() {

  private var adapter: FavoriteAdapter? = null

  @Inject
  lateinit var factory: ViewModelProvider.Factory

  lateinit var favoriteViewModel: FavoriteViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_favorite)

    initViewModel()
    setUpToolbar()
    initRecyclerView()
    initObservers()
  }

  private fun initViewModel() {
    favoriteViewModel = ViewModelProviders.of(this, factory)
        .get(FavoriteViewModel::class.java)
  }

  private fun setUpToolbar() {
    supportActionBar?.title = TITLE
  }

  private fun initRecyclerView() {
    adapter = FavoriteAdapter { performAction(it) }
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(this)
  }

  private fun initObservers() {
    favoriteViewModel.favoritesItems.observe(this, Observer {
      if (it.isNotEmpty()) {
        adapter?.setItems(it)
      }
    })
  }

  private fun performAction(favoriteItem: FavoriteItem) {
    // TODO open detail
  }

  companion object {
    private const val TITLE = "Favorites"
  }
}