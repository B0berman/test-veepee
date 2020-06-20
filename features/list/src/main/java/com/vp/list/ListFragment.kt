package com.vp.list

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vp.list.GridPagingScrollListener.LoadMoreItemsListener
import com.vp.list.viewmodel.ListState
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.model.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment(), LoadMoreItemsListener, ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var listViewModel: ListViewModel
    private lateinit var gridPagingScrollListener: GridPagingScrollListener
    private lateinit var listAdapter: ListAdapter
    private lateinit var viewAnimator: ViewAnimator
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var loadingMoreProgressBar: ProgressBar

    private lateinit var detailUriBuilder: Uri.Builder
    private lateinit var currentQuery: String
    private val initialQuery = "Interview"

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
        loadingMoreProgressBar = view.findViewById(R.id.loadingMoreProgressBar)

        detailUriBuilder = Uri.Builder()
                .authority("movies")
                .scheme("app")
                .path("/detail")

        currentQuery = savedInstanceState?.getString(CURRENT_QUERY) ?: initialQuery

        initBottomNavigation(view)
        initList()
        listViewModel.observeMovies().observe(this, Observer { searchResult ->
            searchResult?.let { handleResult(listAdapter, it) }
        })
        loadData(currentQuery)
    }

    private fun initBottomNavigation(view: View) {
        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.favorites) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
                intent.setPackage(requireContext().packageName)
                startActivity(intent)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter.setOnItemClickListener(this)
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView.layoutManager = layoutManager

        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        gridPagingScrollListener.setLoadMoreItemsListener(this)
        recyclerView.addOnScrollListener(gridPagingScrollListener)
    }

    private fun showProgressBar() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(recyclerView)
    }

    private fun showError() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(errorTextView)
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        when (searchResult.listState) {
            ListState.LOADED -> {
                setItemsData(listAdapter, searchResult)
                loadingMoreProgressBar.visibility = View.GONE
                showList()
            }
            ListState.IN_PROGRESS -> showProgressBar()
            else -> {
                loadingMoreProgressBar.visibility = View.GONE
                showError()
            }
        }
        gridPagingScrollListener.markLoading(false)
    }

    private fun loadData(query: String) {
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.setItems(searchResult.items)
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
        loadingMoreProgressBar.visibility = View.VISIBLE
        listViewModel.searchMoviesByTitle(currentQuery, page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter.clearItems()
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    fun reloadHomePage() {
        recyclerView.scrollToPosition(0)
        currentQuery = initialQuery
        loadData(initialQuery)
    }

    override fun onItemClick(imdbID: String?) {
        detailUriBuilder.clearQuery()
        val detailData = detailUriBuilder
                .appendQueryParameter("imdbID", imdbID)
                .build()
        val detailActivity = Intent()
                .setAction(Intent.ACTION_VIEW)
                .setData(detailData)
        startActivity(detailActivity)
    }

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
    }
}
