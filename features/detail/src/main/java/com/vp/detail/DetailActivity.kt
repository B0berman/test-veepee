package com.vp.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel: DetailsViewModel
    private lateinit var movieId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        movieId = intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }

        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel

        binding.setLifecycleOwner(this)

        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.fetchDetails(movieId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)

        if (detailViewModel.isFavorite(movieId)) {
            menu?.findItem(R.id.star)?.setIcon(R.drawable.ic_star_full)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.star -> {
                if (detailViewModel.isFavorite(movieId)) {
                    detailViewModel.removeFromFavorite(movieId)
                } else {
                    detailViewModel.saveToFavorite(movieId)
                }

                invalidateOptionsMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
