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

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: DetailsViewModel
    private var favoriteMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        viewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        queryProvider = this
        viewModel.fetchDetails()
        subscribeToUiEvents()
    }

    private fun subscribeToUiEvents() {
        viewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })

        viewModel.isFavorite().observe(this, Observer { isFavorite ->
            favoriteMenuItem?.icon = resources.getDrawable(when(isFavorite) {
                true ->  R.drawable.ic_star_black_24dp
                false -> R.drawable.ic_star
            }, null)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        favoriteMenuItem = menu?.findItem(R.id.star)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.star -> {
                viewModel.storeMovie()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getMovieId(): String {
        return intent?.extras?.getString(EXTRA_MOVIE_ID) ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    companion object {
        private const val EXTRA_MOVIE_ID = "imdbID"
        lateinit var queryProvider: QueryProvider

        fun generateIntent(context: Context, movieID: String): Intent {
            return Intent(context, DetailActivity::class.java)
                .putExtra(EXTRA_MOVIE_ID, movieID)
        }
    }
}
