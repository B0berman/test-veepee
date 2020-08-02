package com.vp.list

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

private const val CURRENT_QUERY = "current_query"

class ListFragment : Fragment() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val listViewModel: ListViewModel by lazy{
        ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
    }
    private var gridPagingScrollListener: GridPagingScrollListener? = null
    private lateinit var listAdapter: ListAdapter
    private var currentQuery = "Interview"

    companion object{
        const val TAG = "ListFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentQuery = savedInstanceState?.getString(CURRENT_QUERY) ?: currentQuery
        initBottomNavigation(view)
        initReloadButton()
        initList()
        listViewModel.observeMovies().observe(this, Observer { searchResult: SearchResult? ->
            searchResult?.let { handleResult(listAdapter, it) }
        })
        listViewModel.searchMoviesByTitle(currentQuery, 1)
        showProgressBar()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    private fun initReloadButton() {
        reloadButton.setOnClickListener { listViewModel.searchMoviesByTitle(currentQuery, 1) }
    }

    private fun loadMoreItems(page: Int) {
        gridPagingScrollListener?.markLoading(true)
        listViewModel.searchMoviesByTitle(currentQuery, page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter.clearItems()
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    private fun initBottomNavigation(view: View) {
        view.findViewById<BottomNavigationView>(R.id.bottomNavigation).apply{
            setOnNavigationItemSelectedListener { item: MenuItem ->
                if (item.itemId == R.id.favorites) {
                    Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites")).apply{
                        setPackage(requireContext().packageName)
                    }.also (::startActivity )
                }
                true
            }
        }
    }

    private fun initList() {
        listAdapter = ListAdapter { imdbID: String ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
            intent.setPackage(requireContext().packageName)
            startActivity(intent)
        }

        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView!!.layoutManager = layoutManager

        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(
                layoutManager,
                ::loadMoreItems
        ).also(recyclerView::addOnScrollListener)

    }

    private fun showProgressBar() {
        viewAnimator.displayedChild = viewAnimator!!.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator.displayedChild = viewAnimator!!.indexOfChild(recyclerView)
    }

    private fun showError() {
        viewAnimator.displayedChild = viewAnimator!!.indexOfChild(errorContainer)
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
        gridPagingScrollListener?.markLoading(false)
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.setItems(searchResult.items)
        if (searchResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener?.markLastPage(true)
        }
    }

}