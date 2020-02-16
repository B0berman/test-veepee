package com.vp.detail

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.run

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var detailViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.extras?.getString(IMDBID_KEY) ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails(intent?.extras?.getString(IMDBID_KEY) ?: "")
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.isFavourite.observe(this, Observer {
            if (it) invalidateOptionsMenu()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        if (detailViewModel.isFavourite()) {
            menu?.findItem(R.id.star)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_fill)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.star && !detailViewModel.isFavourite()) {
            detailViewModel.addFavourite()
        } else {
            Toast.makeText(this, "Already favourite", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val IMDBID_KEY = "imdbID"
        @JvmStatic
        fun getDetailIntent(context: Context, imdbId: String): Intent =
                Intent(context, DetailActivity::class.java).apply {
                    putExtra(IMDBID_KEY, imdbId)
                }
    }
}
