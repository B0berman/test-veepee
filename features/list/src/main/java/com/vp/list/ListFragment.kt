package com.vp.list

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vp.list.GridPagingScrollListener.LoadMoreItemsListener
import com.vp.list.ListAdapter.OnItemClickListener
import com.vp.list.databinding.FragmentListBinding
import com.vp.list.viewmodel.ListState.IN_PROGRESS
import com.vp.list.viewmodel.ListState.LOADED
import com.vp.list.viewmodel.ListState.LOADING_MORE
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment(R.layout.fragment_list), LoadMoreItemsListener, OnItemClickListener {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val listViewModel: ListViewModel by lazy {
        ViewModelProvider(viewModelStore, factory).get(ListViewModel::class.java)
    }
    private var binding: FragmentListBinding? = null
    private lateinit var gridPagingScrollListener: GridPagingScrollListener
    private var listAdapter: ListAdapter = ListAdapter()
    private var currentQuery = "Interview"

    private fun requireBinding() = binding ?: error("accessing binding with destroyed view")

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY, currentQuery)
        }
        initBottomNavigation(view)
        initList()
        listViewModel.observeMovies().observe(viewLifecycleOwner, { searchResult: SearchResult? ->
            if (searchResult != null) {
                handleResult(listAdapter, searchResult)
            }
        })
        listViewModel.searchMoviesByTitle(currentQuery, 1)
        showProgressBar()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
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
        val binding = requireBinding()
        listAdapter.setOnItemClickListener(this)
        binding.recyclerView.adapter = listAdapter
        binding.recyclerView.setHasFixedSize(true)
        val spanCount =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        val layoutManager = GridLayoutManager(context, spanCount)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return listAdapter.getItemSpanSize(position, spanCount)
            }
        }
        binding.recyclerView.layoutManager = layoutManager

        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        gridPagingScrollListener.setLoadMoreItemsListener(this)
        binding.recyclerView.addOnScrollListener(gridPagingScrollListener)
    }

    private fun showProgressBar() {
        val binding = requireBinding()
        binding.viewAnimator.displayedChild = binding.viewAnimator.indexOfChild(binding.progressBar)
    }

    private fun showList() {
        val binding = requireBinding()
        val viewAnimator = binding.viewAnimator
        viewAnimator.displayedChild = viewAnimator.indexOfChild(binding.recyclerView)
    }

    private fun showError() {
        val binding = requireBinding()
        binding.viewAnimator.displayedChild = binding.viewAnimator.indexOfChild(binding.errorText)
    }

    private fun handleResult(
        listAdapter: ListAdapter,
        searchResult: SearchResult
    ) {
        gridPagingScrollListener.markLoading(false)
        when (searchResult.listState) {
            LOADED -> {
                setItemsData(listAdapter, searchResult)
                showList()
            }
            IN_PROGRESS -> {
                showProgressBar()
            }
            LOADING_MORE -> {
                gridPagingScrollListener.markLoading(true)
                listAdapter.setItems(searchResult.items, true)
                showList()
            }
            else -> {
                showError()
            }
        }
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.setItems(searchResult.items, false)
        if (searchResult.totalResult <= listAdapter.itemCount) {
            Log.d(TAG, "setItemsData: end reached $searchResult ${listAdapter.itemCount}")
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

    fun refresh() {
        listViewModel.refreshMovies()
    }

    override fun onItemClick(imdbID: String) {
        val detailUri = Uri.parse("app://movies/detail")
            .buildUpon()
            .appendQueryParameter("imdbID", imdbID)
            .build()
        val intent = Intent(Intent.ACTION_VIEW, detailUri)
        intent.setPackage(requireContext().packageName)
        startActivity(intent)
    }

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
    }
}
