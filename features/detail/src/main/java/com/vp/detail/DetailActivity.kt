package com.vp.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.model.MovieDetail

import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.run

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private var isFavorite: Boolean = false
    private val detailViewModel: DetailsViewModel by lazy { ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java) }
    lateinit var movie: MovieDetail


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.viewModel = detailViewModel

        queryProvider = this
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails()

        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })


        detailViewModel.details().observe(this, Observer { movie = it })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {


        R.id.star -> {
            if (item.isChecked) {
                item.isChecked = false
                item.setIcon(R.drawable.ic_star)
                detailViewModel.sharedPreference.removeValue(movie.imdbID)


            } else {
                item.isChecked = true
                item.setIcon(R.drawable.ic_star_is_checked)
                detailViewModel.sharedPreference.save(movie.imdbID, movie)
            }

            true
        }


        else -> {

            super.onOptionsItemSelected(item)
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        detailViewModel.favorite().observe(this, Observer {
            isFavorite = it
            if (it) {
                menu?.findItem(R.id.star)?.setIcon(R.drawable.ic_star_is_checked)
                menu?.findItem(R.id.star)?.isChecked = true
            } else {
                menu?.findItem(R.id.star)?.setIcon(R.drawable.ic_star)
                menu?.findItem(R.id.star)?.isChecked = false
            }
        })




        return super.onPrepareOptionsMenu(menu)
    }


    override fun getMovieId(): String {

        return return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    companion object {
        lateinit var queryProvider: QueryProvider
    }
}
