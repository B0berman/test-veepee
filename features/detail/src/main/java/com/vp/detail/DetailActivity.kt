package com.vp.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import com.vp.detail.viewmodel.StarButtonClickListener
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.run

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var starButtonClickListener: StarButtonClickListener
    private var starButtonState = DetailsViewModel.StarButtonState.DEACTIVATED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        val detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        starButtonClickListener = detailViewModel
        binding.viewModel = detailViewModel
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails(getMovieId())
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.starButtonState().observe(this, Observer {
            starButtonState = it
            invalidateOptionsMenu()
        })
        detailViewModel.requestStarButtonState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (starButtonState == DetailsViewModel.StarButtonState.DEACTIVATED) {
            menuInflater.inflate(R.menu.detail_menu, menu)
        } else {
            menuInflater.inflate(R.menu.detail_menu_stared_movie, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
       if (item != null) {
           if (item.itemId == R.id.star) {
               starButtonClickListener.onStarButtonClick()
               return true
           }
       }
        return super.onOptionsItemSelected(item)
    }

    fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }
}
