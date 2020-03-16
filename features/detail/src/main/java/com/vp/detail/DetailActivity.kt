package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import com.vp.detail.viewmodel.MovieViewModel
import com.vp.persistance.model.MovieModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {
    lateinit var detailViewModel: DetailsViewModel

    lateinit var movieViewModel: MovieViewModel
    lateinit var starMenu: MenuItem

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        binding.viewModel = detailViewModel
        queryProvider = this
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails()

        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        starMenu = menu!!.findItem(R.id.star)
        starMenu.setOnMenuItemClickListener { item ->
            if (starMenu.isChecked) {
                movieViewModel.removeItem(getMovieId())
            } else {
                detailViewModel.details().observe(this, Observer {
                    movieViewModel.insert(it)
                })
            }
            true
        }
        movieViewModel.getById(getMovieId()).observe(this, Observer {
            setFavoriteMenuItemIcon(it)
        })
        return true
    }

    fun setFavoriteMenuItemIcon(movie: MovieModel?) {
        if (movie != null) {
            starMenu.setIcon(R.drawable.ic_star_pink)
            starMenu.setChecked(true)
        } else {
            starMenu.setIcon(R.drawable.ic_star)
            starMenu.setChecked(false)
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

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(this).clearMemory()

    }
}
