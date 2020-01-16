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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vp.list.GridPagingScrollListener.LoadMoreItemsListener
import com.vp.list.viewmodel.ListState
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

class ListFragment : Fragment(), LoadMoreItemsListener, ListAdapter.OnItemClickListener {

    @JvmField
    @Inject
    var factory: ViewModelProvider.Factory? = null

    private val listViewModel by lazy {
        ViewModelProviders.of(this, factory).get(
            ListViewModel::class.java
        )
    }
    private var gridPagingScrollListener: GridPagingScrollListener? = null
    private var listAdapter = ListAdapter()
    private var currentQuery: String = "Interview"

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY) ?: ""
        }
        initBottomNavigation(view)
        initList()
        listViewModel.observeMovies().observe(
            this,
            Observer { searchResult: SearchResult? ->
                searchResult?.let { handleResult(it) }
            }
        )
        listViewModel.searchMoviesByTitle(currentQuery, 1)
        showProgressBar()
        initListeners()
    }

    private fun initListeners() {
        swipeRefresh.setOnRefreshListener { submitSearchQuery(currentQuery) }
    }

    private fun initBottomNavigation(view: View) {
        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem? ->
            if (item?.itemId == R.id.favorites) {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
                intent.setPackage(requireContext().packageName)
                startActivity(intent)
            }
            true
        }
    }

    private fun initList() {
        listAdapter.setOnItemClickListener(this)
        recyclerView?.adapter = listAdapter
        recyclerView?.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(
            context,
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        )
        recyclerView?.layoutManager = layoutManager

        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        gridPagingScrollListener?.let {
            it.setLoadMoreItemsListener(this)
            recyclerView?.addOnScrollListener(it)
        }
    }

    private fun showProgressBar() {
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(progressBar) ?: -1
    }

    private fun showList() {
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(swipeRefresh) ?: -1
    }

    private fun showError() {
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(errorText) ?: -1
    }

    private fun handleResult(
        searchResult: SearchResult
    ) {
        when (searchResult.listState) {
            ListState.LOADED -> {
                setItemsData(listAdapter, searchResult)
                showList()
            }
            ListState.IN_PROGRESS -> {
                showProgressBar()
            }
            else -> {
                showError()
            }
        }
        gridPagingScrollListener?.markLoading(false)
        swipeRefresh?.isRefreshing = false
    }

    private fun setItemsData(
        listAdapter: ListAdapter,
        searchResult: SearchResult
    ) {
        listAdapter.setItems(searchResult.items)
        if (searchResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener?.markLastPage(true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    override fun loadMoreItems(page: Int) {
        gridPagingScrollListener?.markLoading(true)
        listViewModel.searchMoviesByTitle(currentQuery, page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter.clearItems()
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    override fun onItemClick(imdbID: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("app://movies/detail?imdbID=$imdbID")
        )
        intent.setPackage(requireContext().packageName)
        startActivity(intent)
    }

    companion object {
        val TAG: String? = "ListFragment"
        private val CURRENT_QUERY: String? = "current_query"
    }
}