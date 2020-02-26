package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import com.vp.favorites.model.MovieItem
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        val detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.lifecycleOwner = this
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        binding.viewModel?.start(getMovieId())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        binding.viewModel?.movieItem?.observe(this, Observer { item: MovieItem? ->
            menu?.getItem(0)?.setIcon(if (item != null )R.drawable.ic_start_filled else R.drawable.ic_star);
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle item selection
        return when (item.itemId) {
            R.id.star -> {
                saveFavoriteMovie()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveFavoriteMovie() {
        binding.viewModel?.updateFavoriteStatus()
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    companion object {
        lateinit var queryProvider: QueryProvider
    }
}
