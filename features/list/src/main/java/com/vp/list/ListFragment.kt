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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vp.list.GridPagingScrollListener.LoadMoreItemsListener
import com.vp.list.viewmodel.ListState
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment(), LoadMoreItemsListener, ListAdapter.OnItemClickListener, View.OnClickListener {
    @JvmField
    @Inject
    var factory: ViewModelProvider.Factory? = null
    private lateinit var srl_Refresh: SwipeRefreshLayout
    private var listViewModel: ListViewModel? = null
    private var gridPagingScrollListener: GridPagingScrollListener? = null
    private var viewAnimator: ViewAnimator? = null
    private lateinit var floatingFavorites: FloatingActionButton
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var progressBarNextPage: ProgressBar? = null
    private var errorTextView: TextView? = null
    private var currentQuery = "Interview"
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
        floatingFavorites = view.findViewById(R.id.floatingFavorites)
        recyclerView = view.findViewById(R.id.recyclerView)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        progressBar = view.findViewById(R.id.progressBar)
        progressBarNextPage = view.findViewById(R.id.progressBarNextPage)
        errorTextView = view.findViewById(R.id.errorText)
        srl_Refresh = view.findViewById(R.id.srl_Refresh)
        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY)
        }

        initList()
        listViewModel!!.observeMovies().observe(this, Observer { searchResult: SearchResult? ->
            this.srl_Refresh.isRefreshing = false
            searchResult?.let { handleResult(it) }
        })
        listViewModel!!.searchMoviesByTitle(currentQuery, 1)
        showProgressBar()
        srl_Refresh.setOnRefreshListener(OnRefreshListener {
            srl_Refresh.isRefreshing = true
            listViewModel!!.searchMoviesByTitle(currentQuery, 1)
        })
        floatingFavorites.setOnClickListener(this)
    }

    private fun initList() {
        val listAdapter = ListAdapter()
        listAdapter.setOnItemClickListener(this)
        recyclerView!!.adapter = listAdapter
        recyclerView!!.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView!!.layoutManager = layoutManager

        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        gridPagingScrollListener!!.setLoadMoreItemsListener(this)

        gridPagingScrollListener!!.getScrollDown().observe(this, Observer {
            if (it)
                floatingFavorites!!.hide()
            else
                floatingFavorites!!.show()

        })
        recyclerView!!.addOnScrollListener(gridPagingScrollListener!!)
    }

    private fun showProgressBar() {
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(progressBar)
        showProgressBarNextPage()
    }

    private fun showProgressBarNextPage() {
        progressBarNextPage!!.visibility = View.VISIBLE
    }

    private fun hideProgressBarNextPage() {
        progressBarNextPage!!.visibility = View.GONE
    }

    private fun showList() {
        hideProgressBarNextPage()
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(recyclerView)
    }

    private fun showError() {
        hideProgressBarNextPage()
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(errorTextView)
    }

    private fun handleResult(searchResult: SearchResult) {
        when (searchResult.listState) {
            ListState.LOADED -> {
                setItemsData(searchResult)
                showList()
            }
            ListState.IN_PROGRESS -> {
                srl_Refresh.isRefreshing = true
                showProgressBar()
            }
            else -> {
                showError()
            }
        }
        gridPagingScrollListener!!.markLoading(false)
    }

    private fun setItemsData(searchResult: SearchResult) {
        (recyclerView!!.adapter as ListAdapter?)!!.setItems(searchResult.items)
        if (searchResult.items.isEmpty()) {
            gridPagingScrollListener!!.markLastPage(true)
        } else {
            gridPagingScrollListener!!.markLastPage(false)

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    override fun loadMoreItems(page: Int) {
        gridPagingScrollListener!!.markLoading(true)
        showProgressBarNextPage()
        listViewModel!!.searchMoviesByTitle(currentQuery, page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        (recyclerView!!.adapter as ListAdapter?)!!.clearItems()
        listViewModel!!.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    override fun onItemClick(imdbID: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView!!.adapter = null
    }

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.floatingFavorites) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
            intent.setPackage(requireContext().packageName)
            startActivity(intent)
        }
    }
}