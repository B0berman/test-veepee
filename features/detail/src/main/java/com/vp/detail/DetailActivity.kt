package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.model.MovieDetail
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detail: MovieDetail
    private lateinit var detailViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            title = it
            supportActionBar?.title = it
        })
        detailViewModel.details().observe(this, Observer {
            detail = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.star -> onFavoriteButtonClick()
            }
        }
        return true
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    private fun onFavoriteButtonClick() {
        if (detailViewModel.isFavoriteMovieExist(getMovieId())) {
            detailViewModel.deleteToFavoriteMovie(getMovieId())
            toast(R.string.favorite_remove_text)
        } else {
            detailViewModel.addToFavoriteMovie(getMovieId(), detail.title, detail.year, detail.poster)
            toast(R.string.favorite_add_text)
        }
    }

    private fun toast(stringId: Int) {
        runOnUiThread {
            Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        lateinit var queryProvider: QueryProvider
    }
}
