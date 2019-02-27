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
import com.vp.persistence.FavouriteMovie
import com.vp.persistence.FavouritesDaoFile
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.run

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var currentDetails: MovieDetail? = null
    private var currentImdbID: String? = null

    private val favouriteDao = FavouritesDaoFile(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        val detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.details().observe(this, Observer {
            currentDetails = it
        })
        currentImdbID = getMovieId()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.star)?.apply {
            setFavourite(currentImdbID?.let { favouriteDao.isFavourite(it) } ?: false)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.star -> {
                currentImdbID?.let {
                    if (item.isFavourite()) {
                        favouriteDao.removeFavourite(it)
                        item.setFavourite(false)
                    } else {
                        val movie = FavouriteMovie(
                                it,
                                currentDetails?.title,
                                currentDetails?.year,
                                currentDetails?.director)
                        favouriteDao.addFavourite(movie)
                        item.setFavourite(true)
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun MenuItem.setFavourite(favourite: Boolean) {
        if (!favourite) {
            icon = resources.getDrawable(R.drawable.ic_star)
            isChecked = false
        } else {
            icon = resources.getDrawable(R.drawable.ic_star_selected)
            isChecked = true
        }
    }

    private fun MenuItem.isFavourite() = isChecked

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    companion object {
        lateinit var queryProvider: QueryProvider
    }
}
