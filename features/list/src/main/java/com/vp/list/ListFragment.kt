package com.vp.list

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vp.list.GridPagingScrollListener.LoadMoreItemsListener
import com.vp.list.R.layout
import com.vp.list.viewmodel.ListState.IN_PROGRESS
import com.vp.list.viewmodel.ListState.LOADED
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_list.errorText
import kotlinx.android.synthetic.main.fragment_list.progressBar
import kotlinx.android.synthetic.main.fragment_list.recyclerView
import kotlinx.android.synthetic.main.fragment_list.swipeRefresh
import kotlinx.android.synthetic.main.fragment_list.viewAnimator
import javax.inject.Inject

class ListFragment : Fragment(), LoadMoreItemsListener {

  @Inject
  lateinit var factory: Factory

  private lateinit var listViewModel: ListViewModel
  private lateinit var gridPagingScrollListener: GridPagingScrollListener
  private lateinit var listAdapter: ListAdapter
  private var currentQuery = "Interview"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AndroidSupportInjection.inject(this)
    listViewModel = ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(layout.fragment_list, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (savedInstanceState != null) {
      currentQuery = savedInstanceState.getString(CURRENT_QUERY)
    }
    initBottomNavigation(view)
    initList()
    listViewModel.observeMovies()
        .observe(this, Observer { searchResult: SearchResult? ->
          swipeRefresh.isRefreshing = false
          if (searchResult != null) {
            handleResult(listAdapter, searchResult)
          }
        }
        )
    listViewModel.searchMoviesByTitle(currentQuery, 1)
    showProgressBar()
    initSwipeRefresh()
  }

  private fun initSwipeRefresh() {
    swipeRefresh.setOnRefreshListener {
      swipeRefresh.isRefreshing = true
      listViewModel.searchMoviesByTitle(currentQuery, 1)
    }
  }

  private fun initBottomNavigation(view: View) {
    val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigation)
    bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
      if (item.itemId == R.id.favorites) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
        intent.setPackage(requireContext().packageName)
        startActivity(intent)
      }
      true
    }
  }

  private fun initList() {
    listAdapter = ListAdapter { onItemClick(it) }
    recyclerView.adapter = listAdapter
    recyclerView.setHasFixedSize(true)
    val layoutManager = GridLayoutManager(
        context,
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
    )
    recyclerView.layoutManager = layoutManager
    // Pagination
    gridPagingScrollListener = GridPagingScrollListener(layoutManager)
    gridPagingScrollListener.setLoadMoreItemsListener(this)
    recyclerView.addOnScrollListener(gridPagingScrollListener)
  }

  private fun showProgressBar() {
    viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(progressBar)
  }

  private fun showList() {
    viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(recyclerView)
  }

  private fun showError() {
    viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(errorText)
  }

  private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
    when (searchResult.listState) {
      LOADED -> {
        setItemsData(listAdapter, searchResult)
        showList()
      }
      IN_PROGRESS -> {
        showProgressBar()
      }
      else -> {
        showError()
      }
    }
    gridPagingScrollListener.markLoading(false)
  }

  private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
    listAdapter.setItems(searchResult.items.toMutableList())
    if (searchResult.totalResult <= listAdapter.itemCount) {
      gridPagingScrollListener.markLastPage(true)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(CURRENT_QUERY, currentQuery)
  }

  override fun loadMoreItems(page: Int) {
    gridPagingScrollListener.markLoading(true)
    swipeRefresh.isRefreshing = true
    listViewModel.searchMoviesByTitle(currentQuery, page)
  }

  fun submitSearchQuery(query: String) {
    currentQuery = query
    listAdapter.clearItems()
    listViewModel.searchMoviesByTitle(query, 1)
    showProgressBar()
  }

  private fun onItemClick(imdbID: String) {
    val url = "app://movies/detail?imdbID=$imdbID"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
  }

  companion object {
    const val TAG = "ListFragment"
    private const val CURRENT_QUERY = "current_query"
  }
}