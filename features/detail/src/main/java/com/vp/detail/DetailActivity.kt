package com.vp.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.vp.database.FavoriteStorage
import com.vp.database.model.FavoriteMovie
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var favoritesStorage: FavoriteStorage

    lateinit var detailsViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailsViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailsViewModel
        binding.setLifecycleOwner(this)
        detailsViewModel.fetchDetails(getMovieId())
        detailsViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        launch(UI) {
            val menuItem = menu?.findItem(R.id.favorite)

            if (favoritesStorage.containsMovie(getMovieId())) {
                menuItem?.setIcon(R.drawable.ic_star_filled)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return item?.let {
            if (it.itemId == R.id.favorite) {
                toggleFavorite(it)
            }
            true
        } ?: super.onOptionsItemSelected(item)
    }

    private fun toggleFavorite(item: MenuItem) {
        launch(UI) {
            if (favoritesStorage.containsMovie(getMovieId())) {
                removeFavorite(item)
            } else {
                addFavorite(item)
            }
        }
    }

    private suspend fun removeFavorite(item: MenuItem) {
        val result = favoritesStorage.removeFavorite(getMovieId())

        if (result > 0) {
            item.setIcon(R.drawable.ic_star)
            Toast.makeText(this@DetailActivity, getString(R.string.movie_added_to_favorites), Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun addFavorite(item: MenuItem) {
        val movieDetail = detailsViewModel.details().value

        movieDetail?.let {
            val result = favoritesStorage.insertFavorite(FavoriteMovie(getMovieId(), movieDetail.title))

            if (result > 0) {
                item.setIcon(R.drawable.ic_star_filled)
                Toast.makeText(this@DetailActivity, getString(R.string.movie_removed_from_favorites), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }
}
