package com.vp.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * This activity should be initialized with the **query** "imdbID"
 */
class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel: DetailsViewModel

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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        val favoriteItem = menu?.findItem(R.id.star)

        favoriteItem?.setOnMenuItemClickListener {
            if (detailViewModel.isFavorite().value!!) {
                detailViewModel.unfavoriteMovie()
            } else {
                detailViewModel.favoriteMovie()
            }
            return@setOnMenuItemClickListener true
        }

        detailViewModel.isFavorite().observe(this, Observer {
            if (it) {
                favoriteItem?.icon = getDrawable(R.drawable.ic_star_filled)
            } else {
                favoriteItem?.icon = getDrawable(R.drawable.ic_star)
            }
        })
        return true
    }

    private fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }
}
