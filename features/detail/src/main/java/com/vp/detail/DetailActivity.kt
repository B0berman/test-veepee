package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.properties.Delegates


class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    private var favoriteState: DetailsViewModel.FavoriteStateState
            by Delegates.observable(DetailsViewModel.FavoriteStateState.UNKNOWN) { _, oldValue, newValue ->
                if (newValue != oldValue) {
                    invalidateOptionsMenu()
                }
            }

    private lateinit var detailViewModel: DetailsViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_detail)

        queryProvider = this
        detailViewModel = ViewModelProvider(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        binding.lifecycleOwner = this
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.favoriteState.observe(this, Observer {
            favoriteState = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
            when (favoriteState) {
                DetailsViewModel.FavoriteStateState.FAVORITE -> {
                    menuInflater.inflate(R.menu.detail_menu_favourite, menu)
                    true
                }

                DetailsViewModel.FavoriteStateState.NOT_FAVORITE -> {
                    menuInflater.inflate(R.menu.detail_menu_default, menu)
                    true
                }

                DetailsViewModel.FavoriteStateState.UNKNOWN -> false
            }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when (item?.itemId) {
                R.id.star_unchecked -> {
                    detailViewModel.setAsFavorite()
                    true
                }

                R.id.star_checked -> {
                    detailViewModel.removeFavorite()
                    true
                }

                else -> super.onOptionsItemSelected(item)
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
