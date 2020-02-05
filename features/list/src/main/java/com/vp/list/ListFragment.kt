package com.vp.list

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
import com.vp.list.GridPagingScrollListener.LoadMoreItemsListener
import com.vp.list.viewmodel.ListState
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment(), LoadMoreItemsListener, ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var listViewModel: ListViewModel
    private var gridPagingScrollListener: GridPagingScrollListener? = null
    private lateinit var listAdapter: ListAdapter
    private lateinit var viewAnimator: ViewAnimator
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
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
        errorTextView = view.findViewById(R.id.errorText)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh)

        savedInstanceState?.let { currentQuery = it.getString(CURRENT_QUERY, "") }

        initBottomNavigation(view)
        initList()
        initSwipeRefresh()
        listViewModel.observeMovies().observe(this, Observer { handleResult(listAdapter, it) })
        listViewModel.searchMoviesByTitle(currentQuery, 1)
        showProgressBar()
    }

    private fun initBottomNavigation(view: View) {
        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem? ->
            if (item?.getItemId() == R.id.favorites) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
                intent.setPackage(requireContext().packageName)
                startActivity(intent)
            }
            true
        }
    }

    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter.onItemClickListener = this
        recyclerView.setAdapter(listAdapter)
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(
            context, if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        )
        recyclerView.setLayoutManager(layoutManager)

        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager).also {
            it.loadMoreItemsListener = this
            recyclerView.addOnScrollListener(it)
        }
    }

    private fun initSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener { listViewModel.refreshMovies(currentQuery) }
    }

    private fun showProgressBar() {
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(progressBar))
    }

    private fun showList() {
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(recyclerView))
    }

    private fun showError() {
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(errorTextView))
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        swipeRefreshLayout.setRefreshing(false)
        when (searchResult.listState) {
            ListState.LOADED -> {
                setItemsData(listAdapter, searchResult)
                showList()
            }
            ListState.IN_PROGRESS -> showProgressBar()
            else -> showError()
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

    override fun onItemClick(imdbID: String?) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("app://movies/detail").buildUpon().appendQueryParameter("imdbID", imdbID).build()
        ).setPackage(requireContext().packageName)
        startActivity(intent)
    }

    companion object {
        val TAG = "ListFragment"
        private val CURRENT_QUERY = "current_query"
    }
}