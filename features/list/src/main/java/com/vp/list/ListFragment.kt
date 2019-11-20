package com.vp.list

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log

import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.lifecycle.Observer
import com.vp.list.viewmodel.ListState

import com.vp.list.viewmodel.SearchResult
import com.vp.list.viewmodel.ListViewModel

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection

class ListFragment : Fragment(), GridPagingScrollListener.LoadMoreItemsListener, ListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, GridPagingScrollListener.VisibleItemListener {

    companion object {
        val TAG = "ListFragment"
        private val CURRENT_QUERY = "current_query"
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val listViewModel by lazy { ViewModelProviders.of(this, factory).get(ListViewModel::class.java) }
    private var gridPagingScrollListener: GridPagingScrollListener? = null
    private val listAdapter by lazy { ListAdapter() }
    private var viewAnimator: ViewAnimator? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var errorTextView: TextView? = null
    private var currentQuery: String = "Interview"
    private var layoutChildCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)
        recyclerView = view.findViewById(R.id.recyclerView)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.errorText)
        swipeRefreshLayout?.setOnRefreshListener(this)


        currentQuery = savedInstanceState?.getString(CURRENT_QUERY) ?: currentQuery


        initBottomNavigation(view)
        initList()
        listViewModel.observeMovies().observe(this, Observer { searchResult ->
            if (searchResult != null) {
                handleResult(listAdapter, searchResult)
            }
        })
        listViewModel.totalMovies().observe(this, onTotalMoviesChange())
        listViewModel.searchMoviesByTitle(currentQuery, 1)
    }

    private fun initBottomNavigation(view: View) {
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.favorites) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
                intent.setPackage(requireContext().packageName)
                startActivity(intent)
            }
            true
        }
    }

    private fun initList() {
        listAdapter.setOnItemClickListener(this)

        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        layoutChildCount = layoutManager.childCount

        recyclerView?.let {
            it.adapter = listAdapter
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
        }

        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager).apply {
            setLoadMoreItemsListener(this@ListFragment)
            setLastItemVisibleListener(this@ListFragment)
            recyclerView?.addOnScrollListener(this)
            recyclerView?.addOnChildAttachStateChangeListener(this)
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (swipeRefreshLayout?.isRefreshing == true) {
            swipeRefreshLayout?.isRefreshing = false
        }
        progressBar?.isIndeterminate = isLoading
    }

    private fun showList() {
        viewAnimator?.let {
            it.displayedChild = it.indexOfChild(swipeRefreshLayout)
        }
    }

    private fun showError() {
        viewAnimator?.let {
            it.displayedChild = it.indexOfChild(errorTextView)
        }
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        when (searchResult.listState) {
            ListState.LOADED -> {
                setItemsData(listAdapter, searchResult)
                showProgressBar(false)
                showList()
            }
            ListState.IN_PROGRESS -> showProgressBar(true)
            else -> {
                showProgressBar(false)
                showError()
            }
        }
        gridPagingScrollListener?.markLoading(false)
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.setItems(searchResult.items)

        gridPagingScrollListener?.markLastPage(searchResult.totalResult <= listAdapter.itemCount)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    override fun loadMoreItems(page: Int) {
        gridPagingScrollListener?.markLoading(true)
        showProgressBar(true)
        listViewModel.searchMoviesByTitle(currentQuery, page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter.clearItems()
        listViewModel.searchMoviesByTitle(query, 1)
    }

    override fun onItemClick(imdbID: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/details?imdbID=$imdbID"))
        intent.setPackage(requireContext().packageName)
        startActivity(intent)
    }

    override fun onLastItemVisible(position: Int) {
        Log.d(TAG, "Last visible movie $position")
        progressBar?.progress = position
    }

    override fun onChildVisibleChange(visibleItems: Int) {
        layoutChildCount = visibleItems
    }

    override fun onRefresh() {
        listAdapter.clearItems()
        listViewModel.searchMoviesByTitle(currentQuery, 1, true)
    }

    private fun onTotalMoviesChange(): Observer<in Int> {
        return Observer {
            Log.d(TAG, "Total movies $it")
            progressBar?.let { bar ->
                bar.max = it
                bar.progress = layoutChildCount
            }
        }
    }
}
