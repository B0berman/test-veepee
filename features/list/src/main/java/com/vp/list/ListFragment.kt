package com.vp.list

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.lifecycle.Observer
import com.vp.list.viewmodel.ListState

import com.vp.list.viewmodel.SearchResult
import com.vp.list.viewmodel.ListViewModel
import com.vp.navigation.NavigationHelper

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection

class ListFragment : Fragment(),
        GridPagingScrollListener.LoadMoreItemsListener,
        ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    @Inject
    lateinit var navigationHelper: NavigationHelper

    private val listViewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
    }
    private var gridPagingScrollListener: GridPagingScrollListener? = null
    private var listAdapter: ListAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var viewAnimator: ViewAnimator? = null
    private var recyclerView: RecyclerView? = null
    private var errorTextView: TextView? = null
    private var currentQuery: String = "Interview"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        errorTextView = view.findViewById(R.id.errorText)

        if (savedInstanceState != null)
            currentQuery = savedInstanceState.getString(CURRENT_QUERY) ?: currentQuery

        swipeRefreshLayout?.setOnRefreshListener { listViewModel.refresh() }

        initBottomNavigation(view)
        initList()
        listViewModel.movies.observe(viewLifecycleOwner, Observer { searchResult ->
            if (searchResult != null) {
                listAdapter?.let { handleResult(it, searchResult) }
            }
        })
        listViewModel.searchMoviesByTitle(currentQuery, 1)
        showProgressBar()
    }

    private fun initBottomNavigation(view: View) {
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.favorites) navigationHelper.launchFavorites()
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
        gridPagingScrollListener?.let {
            it.setLoadMoreItemsListener(this)
            recyclerView?.addOnScrollListener(it)
        }
    }

    private fun showProgressBar() {
        swipeRefreshLayout?.isRefreshing = true
    }

    private fun showList() {
        swipeRefreshLayout?.isRefreshing = false
        viewAnimator?.let { it.displayedChild = it.indexOfChild(swipeRefreshLayout) }
    }

    private fun showError() {
        swipeRefreshLayout?.isRefreshing = false
        viewAnimator?.let { it.displayedChild = it.indexOfChild(errorTextView) }
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
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
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.setItems(searchResult.items.toMutableList())

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
        listViewModel.searchMoviesByTitle(currentQuery!!, page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter?.clearItems()
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    override fun onItemClick(imdbID: String) {
        navigationHelper.launchDetail(imdbID)
    }

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
    }
}
