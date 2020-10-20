package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var detailViewModel: DetailsViewModel

    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_detail
        )
        detailViewModel = ViewModelProvider(viewModelStore, factory)
            .get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        binding.lifecycleOwner = this
        detailViewModel.fetchDetails(getMovieId())
        detailViewModel.title().observe(this) { supportActionBar?.title = it }
        detailViewModel.isFavorite().observe(this) { isFavorite ->
            this.isFavorite = isFavorite
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menu?.apply {
            val iconRes = if (isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star
            findItem(R.id.star).setIcon(iconRes)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.star) {
            detailViewModel.toggleFavorite()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }
}
