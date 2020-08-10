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
import com.vp.list.viewmodel.ListState
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

class ListFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val listViewModel: ListViewModel by lazy { ViewModelProviders.of(this@ListFragment, factory).get(ListViewModel::class.java) }
    private var gridPagingScrollListener: GridPagingScrollListener? = null
    private var listAdapter: ListAdapter? = null
    private var currentQuery = "InterView" // ANSWER - The Wrong State - I get 401 error for Interview
    // check https://www.omdbapi.com/?s=Interview&page=1&apiKey=37088309

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let { currentQuery = it.getString(CURRENT_QUERY)!! }
        initBottomNavigation(view)
        initList()
        listViewModel.run {
            observeMovies().observe(viewLifecycleOwner, Observer { searchResult: SearchResult? ->
                searchResult?.let { handleResult(listAdapter!!, it) }
            })
            searchMoviesByTitle(currentQuery, 1)
        }
        showProgressBar()
        //ANSWER - Some refreshments - I just added refresh button
        refreshButton.setOnClickListener { listViewModel.searchMoviesByTitle(currentQuery, 1)}
    }

    private fun initBottomNavigation(view: View) {
        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            if (item.itemId == R.id.favorites) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites")).apply {
                    setPackage(requireContext().packageName)
                })
            }
            true
        }
    }

    private fun initList() {
        listAdapter = ListAdapter().apply {
            setOnItemClickListener { onItemClick(it) }
        }
        recyclerView.run {
            setHasFixedSize(true)
            adapter = listAdapter

            val layoutManager = GridLayoutManager(context, if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
            this.layoutManager = layoutManager
            // Pagination
            gridPagingScrollListener = GridPagingScrollListener(layoutManager).apply {
                setLoadMoreItemsListener {loadMoreItems(it)}
            }
            addOnScrollListener(gridPagingScrollListener!!)
        }
    }

    private fun showProgressBar() {
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(recyclerView)
    }

    private fun showError() {
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(errorContainer)
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        when (searchResult.listState) {
            ListState.LOADED -> {
                setItemsData(listAdapter, searchResult)
                showList()
            }
            ListState.IN_PROGRESS -> showProgressBar()
            else -> showError()
        }
        gridPagingScrollListener!!.markLoading(false)
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.setItems(searchResult.items)
        if (searchResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener!!.markLastPage(true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    private fun loadMoreItems(page: Int) {
        gridPagingScrollListener!!.markLoading(true)
        listViewModel.searchMoviesByTitle(currentQuery, page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter?.clearItems()
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    private fun onItemClick(imdbID: String) {
        // ANSWER - The Lost Event - I saw that you use deep link so I did it in this way.
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID")))
    }

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
    }
}