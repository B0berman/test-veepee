package com.vp.favorites

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.data.model.MovieFavorite
import com.vp.favorites.viewmodel.FavoritesViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FavoritesFragment : Fragment(), FavoritesAdapter.OnItemClickListener {
    companion object {
        const val TAG = "FavoriteFragment"
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var viewAnimator: ViewAnimator
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(FavoritesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        progressBar = view.findViewById(R.id.progressBar)
        emptyTextView = view.findViewById(R.id.noFavoritesText)

        initList()
        viewModel.favorites().observe(this, Observer { favoritesList ->
            if (favoritesList.isNotEmpty()) {
                setItemsData(favoritesAdapter, favoritesList)
                showList()
            } else {
                showEmpty()
            }
        })
        loadData()
    }

    private fun initList() {
        favoritesAdapter = FavoritesAdapter()
        favoritesAdapter.setOnItemClickListener(this)
        recyclerView.adapter = favoritesAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun loadData() {
        showProgressBar()
        viewModel.loadFavorites()
    }

    private fun showProgressBar() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(recyclerView)
    }

    private fun showEmpty() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(emptyTextView)
    }


    private fun setItemsData(favoriteAdapter: FavoritesAdapter, favorites: List<MovieFavorite>) {
        favoriteAdapter.setItems(favorites)
    }

    override fun onItemClick(position: Int) {
        //TODO
    }
}
