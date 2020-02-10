package com.vp.list

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.ViewAnimator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vp.list.viewmodel.ListState
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment(), GridPagingScrollListener.LoadMoreItemsListener, ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var listViewModel: ListViewModel
    private lateinit var gridPagingScrollListener: GridPagingScrollListener
    private lateinit var listAdapter: ListAdapter
    private lateinit var viewAnimator: ViewAnimator
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorContainer: View

    private var currentQuery: String = "interview"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        listViewModel = ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        progressBar = view.findViewById(R.id.progressBar)
        errorContainer = view.findViewById(R.id.errorContainer)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY, "interview")
        }

        initBottomNavigation(view)
        initList()
        listViewModel.movies.observe(this, Observer { searchResult ->
            if (searchResult != null) {
                handleResult(listAdapter, searchResult)
            }
        })
        listViewModel.searchMoviesByTitle(currentQuery, 1)
        showProgressBar()

        view.findViewById<View>(R.id.errorReload).setOnClickListener { listViewModel.searchMoviesByTitle(currentQuery, 1) }
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
        listAdapter = ListAdapter().apply {
            setOnItemClickListener(this@ListFragment)
        }
        recyclerView.apply {
            adapter = listAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3).apply {
                gridPagingScrollListener = GridPagingScrollListener(this).apply {
                    setLoadMoreItemsListener(this@ListFragment)
                }
            }
            addOnScrollListener(gridPagingScrollListener)
        }
    }

    private fun showProgressBar() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(recyclerView)
    }

    private fun showError() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(errorContainer)
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
        gridPagingScrollListener.markLoading(false)
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.listItems = searchResult.items.toMutableList()

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
        val uri = Uri.parse("app://movies/detail?imdbID=$imdbID")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(requireContext().packageName)
        startActivity(intent)
    }

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
    }
}
