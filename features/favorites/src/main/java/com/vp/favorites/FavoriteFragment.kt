package com.vp.favorites

import android.app.Activity
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vp.favorites.viewmodel.FavoriteResult
import com.vp.favorites.viewmodel.FavoriteState
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FavoriteFragment : Fragment(), GridPagingScrollListener.LoadMoreItemsListener, ListAdapter.OnItemClickListener {

    var factory: ViewModelProvider.Factory? = null
        @Inject set(value) {
            field = value
        }

    private var favoriteViewModel: FavoriteViewModel? = null

    private var gridPagingScrollListener: GridPagingScrollListener? = null
    private var listAdapter: ListAdapter? = null
    private var viewAnimator: ViewAnimator? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var errorTextView: TextView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        favoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        viewAnimator = view.findViewById<ViewAnimator>(R.id.viewAnimator)
        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        errorTextView = view.findViewById<TextView>(R.id.errorText)
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh)
        initBottomNavigation(view)
        initList()
        favoriteViewModel?.movieItem?.observe(viewLifecycleOwner, Observer<FavoriteResult> { favoriteResult: FavoriteResult? ->
            if (favoriteResult != null) {
                listAdapter?.let {
                    handleResult(it, favoriteResult)
                }
            }
        })
        favoriteViewModel?.loadFavoriteMoviesByTitle(1)
        showProgressBar()
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout?.setOnRefreshListener { favoriteViewModel?.loadFavoriteMoviesByTitle(1) }
        }
    }

    private fun handleResult(listAdapter: ListAdapter, favoriteResult: FavoriteResult) {
        when (favoriteResult.favoriteState) {
            FavoriteState.LOADED -> {
                swipeRefreshLayout?.isRefreshing = false
                errorTextView?.visibility = View.GONE
                setItemsData(listAdapter, favoriteResult)
                showList()
            }
            FavoriteState.IN_PROGRESS -> {
                showProgressBar()
            }
            else -> {
                showError()
            }
        }
        gridPagingScrollListener?.markLoading(false)
    }

    private fun setItemsData(listAdapter: ListAdapter, favoriteResult: FavoriteResult) {
        listAdapter.setItems(favoriteResult.items)
        if (favoriteResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener?.markLastPage(true)
        }
    }

    private fun initBottomNavigation(view: View) {
        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.GONE
    }

    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter?.setOnItemClickListener(this)
        recyclerView?.adapter = listAdapter
        recyclerView?.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView?.let {
            it.layoutManager = layoutManager
        }
        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        gridPagingScrollListener?.setLoadMoreItemsListener(this)
        gridPagingScrollListener?.let {
            recyclerView?.addOnScrollListener(it)
        }
    }

    private fun showProgressBar() {
        viewAnimator?.let {
            viewAnimator?.displayedChild = it.indexOfChild(progressBar)
        }
    }

    private fun showList() {
        viewAnimator?.let {
            viewAnimator?.displayedChild = it.indexOfChild(recyclerView)
        }
    }

    private fun showError() {
        viewAnimator?.let {
            viewAnimator?.displayedChild = it.indexOfChild(errorTextView)
        }
    }

    companion object {
        const val TAG = "FavoriteFragment"
    }

    override fun loadMoreItems(page: Int) {
    }

    override fun onItemClick(imdbID: String?) {
        val activity: Activity? = activity
        if (activity == null || activity.isFinishing) {
            return
        }
        val detailsIntent = Intent(activity, FavoriteActivity::class.java)
        detailsIntent.data = Uri.parse("?imdbID=$imdbID")
        startActivity(detailsIntent)
    }
}