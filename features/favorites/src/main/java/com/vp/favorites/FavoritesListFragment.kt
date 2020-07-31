package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        FavoritesListAdapter{ imdbID ->
            Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID")).apply {
                setPackage(requireContext().packageName)
            }.also(::startActivity)
        }
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
            FavoritesViewModel.LoadingState.InProgress -> showProgressBar()
            is FavoritesViewModel.LoadingState.Loaded -> showList().also {
                setItemsData(favoritesListAdapter, loadingState.favorites)
            }
            FavoritesViewModel.LoadingState.Error-> showError()
        }
    }

    private fun setItemsData(favoritesListAdapter: FavoritesListAdapter, favorites: List<MovieFavorite>) {
        favoritesListAdapter.setItems(favorites)
    }

    private fun initList() {
        recyclerView.adapter = favoritesListAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                } else {
                    GridLayoutManager(context, 2)
                }
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(ItemOffsetDecoration(R.dimen.recycler_offset))

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