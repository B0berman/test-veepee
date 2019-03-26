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
import kotlin.run
import android.view.MenuItem
import co.uk.missionlabs.db.FavouritesDB

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    var isFavourite: Boolean = false
    lateinit var detailViewModel:DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, com.vp.detail.R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })

        detailViewModel.favourite().observe(this, Observer {
            isFavourite = it
            invalidateOptionsMenu();
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.vp.detail.R.menu.detail_menu, menu)
        if(isFavourite){
            menu?.findItem(com.vp.detail.R.id.star)?.setIcon(com.vp.detail.R.drawable.ic_star_filled)
        }else{
            menu?.findItem(com.vp.detail.R.id.star)?.setIcon(com.vp.detail.R.drawable.ic_star)
        }

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            com.vp.detail.R.id.star -> {
                detailViewModel.toggleFavourite()
            }

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun getMovieId(): String {
        return intent?.getStringExtra("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details") as Throwable
        }
    }

    companion object {
        lateinit var queryProvider: QueryProvider
    }

    override fun onResume() {
        super.onResume()
        FavouritesDB.loadDB(this)
    }

    override fun onPause() {
        super.onPause()
        FavouritesDB.saveDB(this)
    }
}
