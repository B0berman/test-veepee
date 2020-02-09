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
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var detailViewModel: DetailsViewModel

    private val queryProvider = object : QueryProvider {
        private val queryId by lazy {
            intent?.data?.getQueryParameter("imdbID") ?: throw IllegalStateException("You must provide movie id to display details")
        }

        override fun getMovieId() = queryId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        detailViewModel.bindFavoriteObserver(this, queryProvider.getMovieId())
        binding.viewModel = detailViewModel
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails(queryProvider.getMovieId())
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.favorite().observe(this, Observer {
            invalidateOptionsMenu()
        })

        errorReload.setOnClickListener {
            detailViewModel.fetchDetails(queryProvider.getMovieId())
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val isFavorite = detailViewModel.isFavorite()
        menu?.findItem(R.id.add_favorite)?.isVisible = !isFavorite
        menu?.findItem(R.id.remove_favorite)?.isVisible = isFavorite
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.add_favorite -> detailViewModel.saveToFavorites(queryProvider.getMovieId())
            R.id.remove_favorite -> detailViewModel.removeFromFavorites(queryProvider.getMovieId())
        }

        return super.onOptionsItemSelected(item)
    }
}
