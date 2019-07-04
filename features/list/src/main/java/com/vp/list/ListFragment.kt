package com.vp.list

import android.content.Intent
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewAnimator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.vp.list.databinding.FragmentListBinding
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.model.ListItem
import com.vp.list.viewmodel.model.ListState
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private val listViewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
    }

    private lateinit var binding: FragmentListBinding
    private lateinit var gridPagingScrollListener: GridPagingScrollListener
    private lateinit var listAdapter: ListAdapter

    private var currentQuery: String = DEFAULT_QUERY
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(viewLifecycleOwner)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY, DEFAULT_QUERY)
        }

        initBottomNavigation()
        initList()
        initRefresh()

        with(listViewModel) {
            hasMorePages().observe(viewLifecycleOwner, Observer { hasMorePages ->
                hasMorePages?.let { morePages ->
                    gridPagingScrollListener.isLastPage = morePages.not()
                    listAdapter.setHasMorePages(morePages)
                }
            })
            getItems().observe(viewLifecycleOwner, Observer { items -> items?.show() })
            getState().observe(viewLifecycleOwner, Observer { state -> state?.show() })
            submitQuery(currentQuery)
        }

        showProgressBar()
    }

    private fun initBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.favorites) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
                intent.setPackage(requireContext().packageName)
                startActivity(intent)
            }
            true
        }
    }

    private fun initList() = with(binding) {
        listAdapter = ListAdapter(::onItemClick)

        val columns = if (resources.configuration.orientation == ORIENTATION_PORTRAIT) 2 else 3
        val layoutMgr = GridLayoutManager(context, columns).apply {
            spanSizeLookup = listAdapter.getSpanSizeLookup(columns)
        }

        recyclerView.apply {
            adapter = listAdapter
            setHasFixedSize(true)
            layoutManager = layoutMgr
        }

        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutMgr, ::loadMoreItems)
        recyclerView.addOnScrollListener(gridPagingScrollListener)
    }

    private fun initRefresh() = with(binding.swipeRefreshLayout) {
        setOnRefreshListener { listViewModel.refresh() }
    }

    private fun ListState.show() {
        val loading = when (this) {
            ListState.IN_PROGRESS -> true
            ListState.LOADED, ListState.ERROR -> false
        }
        gridPagingScrollListener.isLoading = loading

        with(binding.swipeRefreshLayout) {
            if (isRefreshing && !loading) isRefreshing = false
        }

        when (this) {
            ListState.IN_PROGRESS -> showProgressBar()
            ListState.LOADED -> showList()
            ListState.ERROR -> showError()
        }
    }

    private fun List<ListItem>.show() {
        listAdapter.items = this
        showList()
    }

    private fun showProgressBar() = with(binding) {
        when {
            !listAdapter.hasItems() && !swipeRefreshLayout.isRefreshing -> {
                viewAnimator.show(progressBar)
                // Prevent triggering additional refresh by disabling swipeRefreshLayout
                swipeRefreshLayout.isEnabled = false
            }
            else -> {
                // Do nothing, because either:
                // 1. Next page is loading and we already have some content to show
                // 2. User used pull-to-refresh and swipeRefreshLayout is already showing ProgressBar
            }
        }
    }

    private fun showList() = with(binding) {
        swipeRefreshLayout.isEnabled = true
        viewAnimator.show(recyclerView)
    }

    private fun showError() = with(binding) {
        swipeRefreshLayout.isEnabled = true
        if (!listAdapter.hasItems()) {
            snackbar?.dismiss()
            viewAnimator.show(errorText)
        } else {
            showErrorSnackbar()
        }
    }

    private fun showErrorSnackbar() {
        if (snackbar != null) return

        snackbar = Snackbar
                .make(binding.root, R.string.data_loading_error, Snackbar.LENGTH_INDEFINITE)
                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        if (snackbar == transientBottomBar) snackbar = null
                    }
                })
                .apply { show() }
    }

    private fun ViewAnimator.show(child: View) {
        displayedChild = indexOfChild(child)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    private fun loadMoreItems(page: Int) {
        gridPagingScrollListener.isLoading = true
        listViewModel.load(page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listViewModel.submitQuery(query)
    }

    fun onItemClick(imdbID: String) {
        val uri = Uri.Builder()
                .scheme("app")
                .authority("movies")
                .appendPath("detail")
                .appendQueryParameter("imdbID", imdbID)
                .build()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "Interview"
    }
}
