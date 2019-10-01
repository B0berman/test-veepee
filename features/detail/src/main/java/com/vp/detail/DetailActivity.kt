package com.vp.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.vp.database.AppDatabase
import com.vp.database.entity.FavoriteEntity
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.run

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel : DetailsViewModel

    private lateinit var favoriteMenuItem : MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        favoriteMenuItem = menu.getItem(0)
        setFavoriteIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.star -> {
                if(favoriteMenuItem.isChecked){
                    unfavoriteMovie()
                } else {
                    favoriteMovie()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun favoriteMovie(){
        detailViewModel.saveFavorite()
    }

    private fun unfavoriteMovie(){
        detailViewModel.removeFavorite()
    }

    private fun setFavoriteIcon(){
        detailViewModel.getFavorite()?.observe(this, Observer {
            favoriteMovie ->
            if(favoriteMovie.isNotEmpty()){
                favoriteMenuItem.isChecked = true
                favoriteMenuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_favorite)
            } else {
                favoriteMenuItem.isChecked = false
                favoriteMenuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_star)
            }
        })
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    //TODO: Memory Leaks Bonus Task (2)
    override fun onDestroy() {
        super.onDestroy()
        queryProvider = null
    }

    //TODO: Memory Leaks Bonus Task (1)
    companion object {
        var queryProvider: QueryProvider? = null
    }
}
