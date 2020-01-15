package com.vp.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewAnimator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vp.favorites.viewmodel.ListState.IN_PROGRESS
import com.vp.favorites.viewmodel.ListState.LOADED
import com.vp.favorites.viewmodel.ListViewModel
import com.vp.favorites.viewmodel.FavoriteFetchResult
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

class ListFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var listViewModel: ListViewModel? = null
    private var listAdapter = ListAdapter()
    private var viewAnimator: ViewAnimator? = null
    private var currentQuery = "Interview"

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        listViewModel =
            ViewModelProviders.of(this, factory).get(
                ListViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        listViewModel?.observeMovies()?.observe(this,
            Observer<FavoriteFetchResult> { favoriteFetchResult: FavoriteFetchResult ->
                handleResult(listAdapter, favoriteFetchResult)
            }
        )
        listViewModel?.fetchFavoriteList()
        showProgressBar()
    }

    private fun initList() {
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
    }

    private fun showProgressBar() {
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(progressBar) ?: 0
    }

    private fun showList() {
//        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(swipeRefresh) ?: 0
    }

    private fun showError() {
        viewAnimator?.displayedChild = viewAnimator?.indexOfChild(errorText) ?: 0
    }

    private fun handleResult(
        listAdapter: ListAdapter,
        favoriteFetchResult: FavoriteFetchResult
    ) {
        when (favoriteFetchResult.listState) {
            LOADED -> {
                listAdapter.setItems(favoriteFetchResult.items)
                showList()
            }
            IN_PROGRESS -> {
                showProgressBar()
            }
            else -> {
                showError()
            }
        }
    }
}
