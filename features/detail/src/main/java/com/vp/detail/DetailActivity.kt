package com.vp.detail

import android.content.Context
import android.content.Intent
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
import javax.inject.Inject

private const val EXTRA_MOVIE_ID = "imdbID"

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel: DetailsViewModel

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails(getMovieId())
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.isFavorite().observe(this, Observer { isFavorite ->
            menu?.let {
                if (isFavorite) {
                    it.findItem(R.id.favorite).setIcon(R.drawable.ic_star_filled_24dp)
                } else {
                    it.findItem(R.id.favorite).setIcon(R.drawable.ic_star)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favorite) {
            detailViewModel.toggleFavorite()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMovieId(): String {
        return intent.getStringExtra(EXTRA_MOVIE_ID) ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    companion object {
        @JvmStatic
        fun newIntent(packageContext: Context, movieId: String): Intent {
            val intent = Intent(packageContext, DetailActivity::class.java)
            intent.putExtra(EXTRA_MOVIE_ID, movieId)
            return intent
        }
    }

}
