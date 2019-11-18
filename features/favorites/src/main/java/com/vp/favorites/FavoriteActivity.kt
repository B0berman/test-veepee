package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.databinding.ActivityFavoriteBinding
import com.vp.favorites.model.FavouriteItem
import com.vp.favorites.viewmodel.FavouriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity(), ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val favouriteViewModel by lazy { ViewModelProviders.of(this, factory).get(FavouriteViewModel::class.java) }
    private lateinit var binding: ActivityFavoriteBinding
    private val listAdapter by lazy { ListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite)
        binding.viewModel = favouriteViewModel
        binding.lifecycleOwner = this
        initList()

        favouriteViewModel.favouriteMovies().observe(this, onListChange())
        favouriteViewModel.state().observe(this, onStatusChange())
        favouriteViewModel.loadFavouriteMovies()
    }

    private fun initList() {
        listAdapter.setOnItemClickListener(this)
        binding.recyclerView.adapter = listAdapter
        binding.recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(baseContext,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun onListChange(): Observer<List<FavouriteItem>> {
        return Observer {
            updateListView(it)
        }
    }

    private fun onStatusChange(): Observer<in FavouriteViewModel.LoadingState> {
        return Observer {
            when (it) {
                FavouriteViewModel.LoadingState.ERROR -> showError()
                FavouriteViewModel.LoadingState.IN_PROGRESS -> showProgressBar()
            }
        }
    }

    private fun updateListView(movies: List<FavouriteItem>) {
        if (movies.isNullOrEmpty()) {
            showEmpty()
        } else {
            showList()
            with(listAdapter) {
                clearItems()
                setItems(movies)
            }
        }
    }

    private fun showProgressBar() {
        with(binding.viewAnimator) {
            displayedChild = indexOfChild(binding.progressBar)
        }
    }

    private fun showList() {
        with(binding.viewAnimator) {
            displayedChild = indexOfChild(binding.recyclerView)
        }
    }

    private fun showError() {
        with(binding.viewAnimator) {
            displayedChild = indexOfChild(binding.errorText)
        }
    }

    private fun showEmpty() {
        with(binding.viewAnimator) {
            displayedChild = indexOfChild(binding.emptyListText)
        }
    }

    override fun onItemClick(imdbID: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/details?imdbID=$imdbID"))
        intent.setPackage(packageName)
        startActivity(intent)
    }
}
