package com.vp.detail

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.run

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        val detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })

        sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (isInFavorites(getMovieId())){
            menu?.findItem(R.id.star)?.setIcon(R.drawable.ic_star_yellow)
        } else {
            menu?.findItem(R.id.star)?.setIcon(R.drawable.ic_star)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.star -> {
                if (isInFavorites(getMovieId())){
                    removeFromFavorites(getMovieId())
                    item.setIcon(R.drawable.ic_star)
                } else {
                    addToFavorites(getMovieId())
                    item.setIcon(R.drawable.ic_star_yellow)
                }

                true
            }
            else -> false
        }
    }

    private fun isInFavorites(id: String) : Boolean {
        val idsSet = sharedPreferences.getStringSet("movieIds", emptySet())?.toMutableSet()
        return idsSet?.contains(id) == true
    }

    private fun addToFavorites(id: String) {
        sharedPreferences.edit().apply {
            val idsSet = sharedPreferences.getStringSet("movieIds", emptySet())?.toMutableSet()
            idsSet?.add(id)
            putStringSet("movieIds", idsSet)
            apply()
        }
    }

    private fun removeFromFavorites(id: String) {
        sharedPreferences.edit().apply {
            val idsSet = sharedPreferences.getStringSet("movieIds", emptySet())?.toMutableSet()
            idsSet?.remove(id)
            putStringSet("movieIds", idsSet)
            apply()
        }
    }

    private fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }
}
