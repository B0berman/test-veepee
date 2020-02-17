package com.vp.list

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ViewAnimator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vp.list.GridPagingScrollListener.LoadMoreItemsListener
import com.vp.list.viewmodel.ListState
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment(), LoadMoreItemsListener, ListAdapter.OnItemClickListener {
    @JvmField
    @Inject
    var factory: ViewModelProvider.Factory? = null
    private lateinit var listViewModel: ListViewModel
    private lateinit var gridPagingScrollListener: GridPagingScrollListener
    private lateinit var listAdapter: ListAdapter
    private var viewAnimator: ViewAnimator? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var errorView: LinearLayout? = null
    private var btnReload: Button? = null
    private var currentQuery: String = "Interview"

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
        errorView = view.findViewById(R.id.errorView)
        btnReload = view.findViewById(R.id.btnReload)

        savedInstanceState?.getString(CURRENT_QUERY)?.let {
            currentQuery = it
        }
        initBottomNavigation(view)
        initList()
        btnReload?.setOnClickListener {
            loadList()
            showProgressBar()
        }
        listViewModel.observeMovies().observe(this, Observer { searchResult: SearchResult? ->
            if (searchResult != null) {
                handleResult(listAdapter, searchResult)
            }
        })
        loadList()
        showProgressBar()
    }

    private fun loadList() {
        listViewModel.searchMoviesByTitle(currentQuery, 1)
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
        listAdapter.setOnItemClickListener(this)
        recyclerView?.adapter = listAdapter
        recyclerView?.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView?.layoutManager = layoutManager
        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        gridPagingScrollListener.setLoadMoreItemsListener(this)
        recyclerView?.addOnScrollListener(gridPagingScrollListener)
    }

    private fun showProgressBar() {
        viewAnimator?.displayedChild = viewAnimator!!.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator?.displayedChild = viewAnimator!!.indexOfChild(recyclerView)
    }

    private fun showError() {
        viewAnimator?.displayedChild = viewAnimator!!.indexOfChild(errorView)
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
        listViewModel.searchMoviesByTitle(currentQuery, page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter.clearItems()
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    override fun onItemClick(imdbID: String) {
        startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?id=${imdbID}")).apply {
                    setPackage(requireContext().packageName)
                })
    }

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
    }
}