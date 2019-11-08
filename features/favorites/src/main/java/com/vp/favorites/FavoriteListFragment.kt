package com.vp.favorites

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
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
import com.vp.favorites.viewmodel.FavoriteListState
import com.vp.favorites.viewmodel.FavoriteListViewModel
import com.vp.favorites.viewmodel.FavoriteSearchResult
import com.vp.navigation.NavigationHelper

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection

class FavoriteListFragment : Fragment(),
        FavoriteListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    @Inject
    lateinit var navigationHelper: NavigationHelper

    private val listViewModel: FavoriteListViewModel by lazy {
        ViewModelProviders.of(this, factory).get(FavoriteListViewModel::class.java)
    }
    private var listAdapter: FavoriteListAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var viewAnimator: ViewAnimator? = null
    private var recyclerView: RecyclerView? = null
    private var errorTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        errorTextView = view.findViewById(R.id.errorText)

        swipeRefreshLayout?.setOnRefreshListener { listViewModel.refresh() }

        initList()
        listViewModel.movies.observe(this, Observer { searchResult ->
            if (searchResult != null) {
                listAdapter?.let { handleResult(it, searchResult) }
            }
        })
        showProgressBar()
    }

    private fun initList() {
        listAdapter = FavoriteListAdapter()
        listAdapter?.setOnItemClickListener(this)
        recyclerView?.adapter = listAdapter
        recyclerView?.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView?.layoutManager = layoutManager
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

    private fun handleResult(listAdapter: FavoriteListAdapter, searchResult: FavoriteSearchResult) {
        when (searchResult.favoriteListState) {
            FavoriteListState.LOADED -> {
                setItemsData(listAdapter, searchResult)
                showList()
            }
            FavoriteListState.IN_PROGRESS -> {
                showProgressBar()
            }
            else -> {
                showError()
            }
        }
    }

    private fun setItemsData(listAdapter: FavoriteListAdapter, searchResult: FavoriteSearchResult) {
        listAdapter.setItems(searchResult.items.toMutableList())
    }

    override fun onItemClick(imdbID: String) {
        navigationHelper.launchDetail(imdbID)
    }

    companion object {
        const val TAG = "FavoriteListFragment"
        private const val CURRENT_QUERY = "current_query"
    }
}
