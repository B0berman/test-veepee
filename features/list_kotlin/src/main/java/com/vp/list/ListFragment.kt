package com.vp.list

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vp.detail.DetailActivity
import com.vp.favorites.GridPagingScrollListener
import com.vp.favorites.ListAdapter
import com.vp.list.viewmodel.ListState
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment(), GridPagingScrollListener.LoadMoreItemsListener, ListAdapter.OnItemClickListener {
    var factory: ViewModelProvider.Factory? = null
        @Inject set(value) {
            field = value
        }

    private var listViewModel: ListViewModel? = null
    private var gridPagingScrollListener: GridPagingScrollListener? = null
    private var listAdapter: ListAdapter? = null
    private var viewAnimator: ViewAnimator? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var errorTextView: TextView? = null
    internal var currentQuery = BuildConfig.SEARCH_VIEW_INITIAL_WORD
        private set

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        listViewModel = ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.errorText)
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh)
        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(Constants.CURRENT_QUERY)
        }
        initBottomNavigation(view)
        initList()
        listViewModel?.observeMovies()?.observe(viewLifecycleOwner, Observer<SearchResult> { searchResult: SearchResult ->
            listAdapter?.let {
                handleResult(it, searchResult)
            }
        })
        listViewModel?.searchMoviesByTitle(currentQuery, 1)
        showProgressBar()
        swipeRefreshLayout?.setOnRefreshListener { searchMoviesByTitle(currentQuery, 1) }
    }

    private fun searchMoviesByTitle(title: String, page: Int) {
        listViewModel?.searchMoviesByTitle(title, page)
        errorTextView?.visibility = View.GONE
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
        listAdapter = ListAdapter()
        listAdapter?.setOnItemClickListener(this)
        recyclerView?.adapter = listAdapter
        recyclerView?.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView?.layoutManager = layoutManager
        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        gridPagingScrollListener?.setLoadMoreItemsListener(this)
        gridPagingScrollListener?.let {
            recyclerView?.addOnScrollListener(it)
        }
    }

    private fun showProgressBar() {
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(progressBar) ?: 0
    }

    private fun showList() {
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(recyclerView) ?: 0
    }

    private fun showError() {
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(errorTextView) ?: 0
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        when (searchResult.listState) {
            ListState.LOADED -> {
                swipeRefreshLayout?.isRefreshing = false
                errorTextView?.visibility = View.GONE
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
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.setItems(searchResult.items)
        if (searchResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener?.markLastPage(true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.CURRENT_QUERY, currentQuery)
    }

    fun updateCurrentQuery(newText: String) {
        currentQuery = newText
    }

    override fun loadMoreItems(page: Int) {
        gridPagingScrollListener?.markLoading(true)
        listViewModel?.searchMoviesByTitle(currentQuery, page)
        showProgressBar()
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter?.clearItems()
        listViewModel?.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    override fun onItemClick(imdbID: String) {
        val activity: Activity? = activity
        if (activity == null || activity.isFinishing) {
            return
        }
        val detailsIntent = Intent(activity, DetailActivity::class.java)
        detailsIntent.data = Uri.parse("?imdbID=$imdbID")
        startActivity(detailsIntent)
    }

    companion object {
        const val TAG = "ListFragment"
    }
}
