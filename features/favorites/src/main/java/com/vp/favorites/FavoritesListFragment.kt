package com.vp.favorites

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.model.MovieFavorite
import com.vp.favorites.viewmodel.FavoritesViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_favorites_list.*
import javax.inject.Inject


class FavoritesListFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val favoritesViewModel: FavoritesViewModel by lazy {
        ViewModelProviders.of(this, factory)[FavoritesViewModel::class.java]
    }

    private val favoritesListAdapter: FavoritesListAdapter by lazy{
        FavoritesListAdapter()
    }


    companion object {
        val TAG: String = FavoritesListFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = FavoritesListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_favorites_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initReloadButton()
        initList()
        favoritesViewModel.fetchFavorites()
        showProgressBar()
        favoritesViewModel.state().observe(this, Observer(::handleState))
    }

    private fun handleState(loadingState: FavoritesViewModel.LoadingState) {
        when(loadingState){
            FavoritesViewModel.LoadingState.IN_PROGRESS -> showProgressBar()
            FavoritesViewModel.LoadingState.LOADED -> showList()
            FavoritesViewModel.LoadingState.ERROR -> showError()
        }
    }

    private fun setItemsData(favoritesListAdapter: FavoritesListAdapter, favorites: List<MovieFavorite>) {
        favoritesListAdapter.setItems(favorites)
        if (searchResult.getTotalResult() <= listAdapter.getItemCount()) {
            gridPagingScrollListener.markLastPage(true)
        }
    }

    private fun initList() {
        recyclerView.adapter = favoritesListAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2)
        recyclerView.layoutManager = layoutManager

        /*// Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        gridPagingScrollListener.setLoadMoreItemsListener(this)
        recyclerView.addOnScrollListener(gridPagingScrollListener)*/
    }

    private fun initReloadButton() {
        reloadButton.setOnClickListener{ favoritesViewModel.fetchFavorites() }
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





}