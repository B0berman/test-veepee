package com.vp.detail


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import com.vp.favorites.database.FavoritesMoviesDb
import com.vp.favorites.database.FavoritesMoviesDb.Companion.getInstance
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var detailViewModel: DetailsViewModel
    lateinit var favoritesMoviesDb: FavoritesMoviesDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        favoritesMoviesDb = getInstance(this)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.lifecycleOwner = this
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.isMovieFavorite(getMovieId(), favoritesMoviesDb)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        detailViewModel.isFavorite().observe(this, Observer {
            when (it) {
                true -> menu?.getItem(0)?.setIcon(R.drawable.ic_star_orange)
                false -> menu?.getItem(0)?.setIcon(R.drawable.ic_star)
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.star -> {

                if (detailViewModel.details().value != null) {
                    val movie = detailViewModel.details().value
                    detailViewModel.updateMovie(getMovieId(), movie, favoritesMoviesDb)
                }

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
