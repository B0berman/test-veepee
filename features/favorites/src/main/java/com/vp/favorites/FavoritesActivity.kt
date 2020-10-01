package com.vp.favorites

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.databinding.ActivityFavoritesBinding
import com.vp.favorites.model.ListState
import com.vp.favorites.model.SearchResult
import com.vp.favorites.viewmodel.FavoritesViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorites.*
import javax.inject.Inject

class FavoritesActivity : DaggerAppCompatActivity(), ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var sharedPreferences: SharedPreferences
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFavoritesBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorites)
        favoritesViewModel = ViewModelProviders.of(this, factory).get(FavoritesViewModel::class.java)
        binding.setLifecycleOwner(this)
        supportActionBar?.title = getString(R.string.favorites)

        setupListAdapter()
        setupSwipeRefresh()

        sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE)

        favoritesViewModel.favorites.observe(this, Observer {
            handleResult(listAdapter, it)
        })
    }

    override fun onStart() {
        super.onStart()
        getFavorites()
    }

    private fun getFavorites() {
        val setIds = sharedPreferences.getStringSet("movieIds", null)?.toSet()
        favoritesViewModel.fetchFavorites(setIds)
    }

    private fun setupListAdapter() {
        listAdapter = ListAdapter()
        listAdapter.setOnItemClickListener(this)
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
    }

    private fun setupSwipeRefresh() {
        swipe_refresh.setOnRefreshListener {
            listAdapter.clearItems()
            getFavorites()
            swipe_refresh.isRefreshing = false
        }
    }

    override fun onItemClick(imdbID: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
        intent.setPackage(packageName)
        startActivity(intent)
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        when (searchResult.listState) {
            ListState.LOADED -> {
                listAdapter.setItems(searchResult.items.toMutableList())
                showList()
            }
            ListState.IN_PROGRESS -> {
                showProgressBar()
            }
            ListState.NO_DATA -> {
                showNoFavoritesMessage()
            }
            else -> {
                showError()
            }
        }
    }

    private fun showProgressBar() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(recyclerView)
    }

    private fun showError() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(errorText)
    }

    private fun showNoFavoritesMessage() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(noFavoritesText)
    }
}