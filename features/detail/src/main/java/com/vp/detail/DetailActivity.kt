package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.Action
import com.vp.detail.viewmodel.DetailsViewModel
import com.vp.detail.viewmodel.FavoriteViewModel
import com.vp.detail.viewmodel.ViewState
import com.vp.exhaustive
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val movieId by lazy { intent!!.data!!.getQueryParameter(IMDB_ID)!! }

    private val detailsViewModel by lazy {
        ViewModelProvider(this, factory).get(DetailsViewModel::class.java)
    }

    private val favoriteViewModel by lazy {
        ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)
    }

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        binding.viewModel = detailsViewModel
        binding.lifecycleOwner = this

        observeDetailsViewModel()
        observeFavoriteViewModel()
        detailsViewModel.fetchDetails(movieId)
    }

    private fun observeDetailsViewModel() {
        detailsViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
    }

    private fun observeFavoriteViewModel() {
        favoriteViewModel.viewState(movieId).observe(this, Observer { viewState ->
            when (viewState) {
                is ViewState.IsFavorite -> {
                    isFavorite = viewState.isFavorite
                    invalidateOptionsMenu()
                }
            }.exhaustive
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menu.findItem(R.id.favorited).isVisible = isFavorite
        menu.findItem(R.id.to_favorite).isVisible = !isFavorite
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.to_favorite) {
            favoriteViewModel.onAction(Action.Favorite(movieId))
            return true
        }
        if (item.itemId == R.id.favorited) {
            favoriteViewModel.onAction(Action.RemoveFavorite(movieId))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val IMDB_ID = "id"
    }
}
