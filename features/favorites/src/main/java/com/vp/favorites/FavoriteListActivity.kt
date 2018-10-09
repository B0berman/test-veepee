package com.vp.favorites

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import com.vp.favorites.model.FavoriteResult
import com.vp.favorites.model.FavoriteState
import com.vp.favorites.viewmodel.FavoriteListViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteListActivity : DaggerAppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, FavoriteListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var listAdapter: FavoriteListAdapter

    private lateinit var favoriteListViewModel: FavoriteListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        favoriteListViewModel = ViewModelProviders.of(this, factory).get(FavoriteListViewModel::class.java)

        initList()

        favoriteListViewModel.observeFavorites().observe(this, Observer { favoriteResult ->
            if (favoriteResult != null) {
                handleResult(listAdapter, favoriteResult)
            }
        })

        showProgressBar()
    }

    override fun onResume() {
        super.onResume()
        favoriteListViewModel.getFavoriteList()
    }

    private fun initList() {
        listAdapter = FavoriteListAdapter()
        listAdapter.setOnItemClickListener(this)

        swipeRefreshLayout.setOnRefreshListener(this)

        val layoutManager = GridLayoutManager(this,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)

        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
    }

    private fun showProgressBar() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(swipeRefreshLayout)
    }

    private fun showError() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(errorTextView)
    }

    private fun handleResult(listAdapter: FavoriteListAdapter, favoriteResult: FavoriteResult) {
        swipeRefreshLayout.isRefreshing = false

        when (favoriteResult.state) {
            FavoriteState.LOADED -> {
                listAdapter.setItems(favoriteResult.favoriteMap)
                showList()
            }
            FavoriteState.IN_PROGRESS -> {
                showProgressBar()
            }
            else -> {
                showError()
            }
        }
    }

    override fun onRefresh() {
        favoriteListViewModel.getFavoriteList()
    }

    override fun onItemClick(imdbID: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
        intent.setPackage(packageName)
        startActivity(intent)
    }
}